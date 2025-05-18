import { useState, useEffect, useContext } from "react";
import { Card, Button, Row, Col, Alert, Spinner } from "react-bootstrap";
import "./styles/payment.css";
import { FaMoneyBillWave, FaQrcode } from "react-icons/fa";
import Apis, { endpoints } from "../configs/Apis";
import { MyUserContext } from "../configs/MyContexts";
import { useLocation } from "react-router-dom";

const Payment = () => {
  const [invoices, setInvoices] = useState([]);
  const [selectedItems, setSelectedItems] = useState([]);
  const [paymentMethod, setPaymentMethod] = useState("");
  const [showAlert, setShowAlert] = useState(false);
  const [paymentStatus, setPaymentStatus] = useState(null);
  const [loading, setLoading] = useState(false);
  const [paymentLoading, setPaymentLoading] = useState(false);
  const user = useContext(MyUserContext);
  // const location = useLocation();

  // useEffect(() => {
  //   const query = new URLSearchParams(location.search);
  //   const status = query.get("status");
  //   const error = query.get("error");

  //   if (status === "success") {
  //     setPaymentStatus({ type: "success", message: "Thanh toán thành công!" });
  //     loadInvoices();
  //   } else if (status === "cancel") {
  //     setPaymentStatus({ type: "warning", message: "Thanh toán đã bị hủy." });
  //   } else if (status === "fail") {
  //     setPaymentStatus({ type: "danger", message: "Thanh toán thất bại." });
  //   } else if (error) {
  //     setPaymentStatus({ type: "danger", message: `Lỗi: ${error}` });
  //   }
  // }, [location]);


  const loadInvoices = async () => {
    try {
      setLoading(true);
      const res = await Apis.get(endpoints["invoices"](user.user?.id), {
        headers: {
          Authorization: `Bearer ${user.token}`,
        },
      });
      setInvoices(res.data);
    } catch (error) {
      console.error("Lỗi khi tải danh sách hóa đơn:", error);
      setPaymentStatus({ type: "danger", message: "Lỗi khi tải hóa đơn." });
    } finally {
      setLoading(false);
    }
  };

useEffect(() => {
  console.log("Loading invoices for user", user.user?.id);
  loadInvoices();
}, [user]);


  const handleItemClick = (id) => {
    const invoice = invoices.find((inv) => inv.id === id);
    if (invoice?.status === "PAID") return;
    setSelectedItems((prev) =>
      prev.includes(id) ? prev.filter((item) => item !== id) : [...prev, id]
    );
  };

  const handleMethodChange = (method) => {
    setPaymentMethod(method);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!paymentMethod || selectedItems.length === 0) {
      setShowAlert(true);
      return;
    }

    setShowAlert(false);
    setPaymentLoading(true);

    const selectedInvoices = invoices.filter((inv) =>
      selectedItems.includes(inv.id)
    );
    const totalAmount = selectedInvoices.reduce(
      (sum, inv) => sum + inv.totalAmount,
      0
    );

    try {
      if (paymentMethod === "vnpay") {
        const res = await Apis.post(
          endpoints["payment"],
          {
            invoiceId: selectedItems,
            amount: totalAmount,
          },
          {
            headers: {
              Authorization: `Bearer ${user.token}`,
            },
          }
        );
        console.log("Kết quả thanh toán:", res);
        console.log("URL thanh toán:", res.data);
        window.open(res.data, "_blank");
      } else {
        setPaymentStatus({
          type: "info",
          message: "Thanh toán trực tiếp đã được chọn.",
        });
      }
    } catch (error) {
      console.error("Lỗi khi tạo đơn hàng:", error);
      setPaymentStatus({
        type: "danger",
        message: "Có lỗi xảy ra trong quá trình thanh toán.",
      });
    } finally {
      setPaymentLoading(false);
    }
  };

  const totalAmounts = invoices
    .filter((item) => selectedItems.includes(item.id))
    .reduce((sum, item) => sum + item.totalAmount, 0);

  return (
    <div className="payment-container">
      <h2 className="text-center mb-4">Thanh toán</h2>
      {paymentStatus && (
        <Alert variant={paymentStatus.type}>{paymentStatus.message}</Alert>
      )}
      {showAlert && (
        <Alert variant="danger">
          Vui lòng chọn phương thức và mục thanh toán.
        </Alert>
      )}

      {loading ? (
        <div className="text-center">
          <Spinner animation="border" />
        </div>
      ) : (
        <form onSubmit={handleSubmit}>
          <Row className="mb-4">
            {invoices.map((item) => (
              <Col md={4} key={item.id}>
                <Card
                  className={`payment-card ${
                    selectedItems.includes(item.id) ? "selected" : ""
                  } ${item.status === "PAID" ? "disabled" : ""}`}
                  onClick={() => handleItemClick(item.id)}
                >
                  <Card.Body className="text-center">
                    <Card.Title>
                      {item.description || `Hóa đơn #${item.id}`}
                    </Card.Title>
                    <Card.Text>
                      {item.totalAmount.toLocaleString()} VND
                    </Card.Text>
                    {item.status === "PAID" ? (
                      <div className="text-success fw-bold">Đã thanh toán</div>
                    ) : (
                      <div className="text-danger fw-bold">Chưa thanh toán</div>
                    )}
                  </Card.Body>
                </Card>
              </Col>
            ))}
          </Row>

          <h5>Phương thức thanh toán:</h5>
          <Row className="mb-4">
            <Col md={6}>
              <Card
 Stuart
                className={`payment-method-card ${
                  paymentMethod === "direct" ? "selected" : ""
                }`}
                onClick={() => handleMethodChange("direct")}
              >
                <Card.Body className="text-center">
                  <div className="icon">
                    <FaMoneyBillWave />
                  </div>
                  <Card.Title>Trực tiếp</Card.Title>
                </Card.Body>
              </Card>
            </Col>
            <Col md={6}>
              <Card
                className={`payment-method-card ${
                  paymentMethod === "vnpay" ? "selected" : ""
                }`}
                onClick={() => handleMethodChange("vnpay")}
              >
                <Card.Body className="text-center">
                  <div className="icon">
                    <FaQrcode />
                  </div>
                  <Card.Title>VNPay</Card.Title>
                </Card.Body>
              </Card>
            </Col>
          </Row>

          <h5>Tổng cộng: {totalAmounts.toLocaleString()} VND</h5>

          <Button
            variant="primary"
            type="submit"
            className="mt-3 w-100"
            disabled={paymentLoading}
          >
            {paymentLoading ? (
              <>
                <Spinner
                  as="span"
                  animation="border"
                  size="sm"
                  role="status"
                  aria-hidden="true"
                />{" "}
                Đang xử lý...
              </>
            ) : (
              "Thanh toán"
            )}
          </Button>
        </form>
      )}
    </div>
  );
};

export default Payment;