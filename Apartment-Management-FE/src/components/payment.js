import { useState, useEffect, useContext } from "react";
import { Card, Button, Row, Col, Alert, Spinner, Form } from "react-bootstrap";
import { FaMoneyBillWave, FaQrcode } from "react-icons/fa";
import Apis, { endpoints } from "../configs/Apis";
import { MyUserContext } from "../configs/MyContexts";
import { useLocation } from "react-router-dom";
import "./styles/payment.css";

const Payment = () => {
  const { user, token } = useContext(MyUserContext);
  const location = useLocation();

  const [invoices, setInvoices] = useState([]);
  const [selectedInvoices, setSelectedInvoices] = useState([]);
  const [paymentMethod, setPaymentMethod] = useState("");
  const [paymentStatus, setPaymentStatus] = useState(null);
  const [showAlert, setShowAlert] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [isPaying, setIsPaying] = useState(false);
  const [proofFile, setProofFile] = useState(null);

  // Load invoices
  const fetchInvoices = async () => {
    setIsLoading(true);
    try {
      const res = await Apis.get(endpoints["invoices"](user?.id), {
        headers: { Authorization: `Bearer ${token}` },
      });
      setInvoices(res.data);
    } catch {
      setPaymentStatus({ type: "danger", message: "Lỗi khi tải hóa đơn." });
    } finally {
      setIsLoading(false);
    }
  };

  // Handle VNPay redirect status
  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const status = params.get("status");

    if (status === "success") {
      setPaymentStatus({ type: "success", message: "Thanh toán thành công qua VNPay!" });
      fetchInvoices();
    } else if (status === "cancel") {
      setPaymentStatus({ type: "warning", message: "Bạn đã hủy thanh toán VNPay." });
    } else if (status === "fail") {
      setPaymentStatus({ type: "danger", message: "Thanh toán thất bại. Vui lòng thử lại." });
    }
  }, [location.search]);

  // Initial fetch
  useEffect(() => {
    if (user?.id) fetchInvoices();
  }, [user?.id]);

  const toggleInvoiceSelection = (id) => {
    const invoice = invoices.find((inv) => inv.id === id);
    if (invoice?.status === "PAID") return;

    setSelectedInvoices((prev) =>
      prev.includes(id) ? prev.filter((item) => item !== id) : [...prev, id]
    );
  };

  const calculateTotal = () =>
    invoices
      .filter((inv) => selectedInvoices.includes(inv.id))
      .reduce((sum, inv) => sum + inv.totalAmount, 0);

  const handlePayment = async (e) => {
    e.preventDefault();

    if (!paymentMethod || selectedInvoices.length === 0) {
      setShowAlert(true);
      return;
    }

    setShowAlert(false);
    setIsPaying(true);

    const totalAmount = calculateTotal();

    try {
      if (paymentMethod === "CASH") {
        const res = await Apis.post(
          endpoints["payment-vnpay"],
          { invoiceId: selectedInvoices, amount: totalAmount },
          { headers: { Authorization: `Bearer ${token}` } }
        );
        window.open(res.data, "_blank");
        setPaymentStatus({
          type: "success",
          message: "Mở VNPay để thanh toán. Hãy kiểm tra trạng thái hóa đơn sau.",
        });
      } else if (paymentMethod === "TRANSFER") {
        const formData = new FormData();
        selectedInvoices.forEach((id) => formData.append("invoiceId", id));
        formData.append("method", "TRANSFER");
        formData.append("file", proofFile);

        await Apis.post(endpoints["payment-banking"], formData, {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "multipart/form-data",
          },
        });

        setPaymentStatus({
          type: "success",
          message: "Gửi minh chứng thanh toán thành công.",
        });
      }
      // Reset
      fetchInvoices();
      setSelectedInvoices([]);
      setPaymentMethod("");
      setProofFile(null);
    } catch {
      setPaymentStatus({ type: "danger", message: "Lỗi khi xử lý thanh toán." });
    } finally {
      setIsPaying(false);
    }
  };

  return (
    <div className="payment-container">
      <h2 className="text-center mb-4">Thanh toán</h2>

      {paymentStatus && <Alert variant={paymentStatus.type}>{paymentStatus.message}</Alert>}
      {showAlert && <Alert variant="danger">Vui lòng chọn phương thức và hóa đơn cần thanh toán.</Alert>}

      {isLoading ? (
        <div className="text-center"><Spinner animation="border" /></div>
      ) : (
        <form onSubmit={handlePayment}>
          <Row className="mb-4">
            {invoices.map((inv) => (
              <Col md={4} key={inv.id}>
                <Card
                  className={`payment-card ${selectedInvoices.includes(inv.id) ? "selected" : ""} ${inv.status === "PAID" ? "disabled" : ""}`}
                  onClick={() => toggleInvoiceSelection(inv.id)}
                >
                  <Card.Body className="text-center">
                    <Card.Title>Hóa đơn #{inv.id}</Card.Title>
                    <Card.Text>{inv.totalAmount.toLocaleString()} VND</Card.Text>
                    <div className={inv.status === "PAID" ? "text-success" : "text-danger"}>
                      {inv.status === "PAID" ? "Đã thanh toán" : "Chưa thanh toán"}
                    </div>
                  </Card.Body>
                </Card>
              </Col>
            ))}
          </Row>

          <h5>Phương thức thanh toán:</h5>
          <Row className="mb-4">
            <Col md={6}>
              <Card
                className={`payment-method-card ${paymentMethod === "TRANSFER" ? "selected" : ""}`}
                onClick={() => setPaymentMethod("TRANSFER")}
              >
                <Card.Body className="text-center">
                  <FaQrcode size={32} />
                  <Card.Title>Chuyển khoản MOMO</Card.Title>
                </Card.Body>
              </Card>
            </Col>
            <Col md={6}>
              <Card
                className={`payment-method-card ${paymentMethod === "CASH" ? "selected" : ""}`}
                onClick={() => setPaymentMethod("CASH")}
              >
                <Card.Body className="text-center">
                  <FaMoneyBillWave size={32} />
                  <Card.Title>VNPay</Card.Title>
                </Card.Body>
              </Card>
            </Col>
          </Row>

          {paymentMethod === "TRANSFER" && (
            <div className="mb-4">
              <h5>Số MOMO để chuyển khoản:</h5>
              <p className="momo-number">0988 123 456</p>
              <Form.Group>
                <Form.Label>Minh chứng chuyển khoản (ảnh):</Form.Label>
                <Form.Control
                  type="file"
                  accept="image/*"
                  onChange={(e) => setProofFile(e.target.files[0])}
                />
              </Form.Group>
              {proofFile && <p className="text-info">Ảnh đã chọn: {proofFile.name}</p>}
            </div>
          )}

          <h5>Tổng cộng: {calculateTotal().toLocaleString()} VND</h5>

          <Button
            type="submit"
            variant="primary"
            className="mt-3 w-100"
            disabled={isPaying}
          >
            {isPaying ? (
              <>
                <Spinner animation="border" size="sm" /> Đang xử lý...
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
