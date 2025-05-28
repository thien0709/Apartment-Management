import React, { useEffect, useState, useContext } from "react";
import { MyUserContext } from "../configs/MyContexts";
import Apis, { authApis, endpoints } from "../configs/Apis";
import moment from "moment";
import { useNavigate } from "react-router-dom";

const Card = () => {
  const user = useContext(MyUserContext); 
  console.log("User context:", user);
  const navigator = useNavigate();
  const [userId, setUserId] = useState(null);
  const [cards, setCards] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [showForm, setShowForm] = useState(false);
  const [newCard, setNewCard] = useState({
    holderName: "",
    relationship: "",
  });

  // Điều hướng đến /login nếu chưa đăng nhập
  useEffect(() => {
    if (!user.user?.id) navigator("/login");
  }, [user.user?.id]);

  // Lấy userId từ endpoint current-user
  useEffect(() => {
    const fetchUserId = async () => {
      if (!user) return;

      try {
        const res = await authApis().get(endpoints["current-user"]);
        setUserId(res.data.id);
      } catch (err) {
        console.error("Lỗi khi lấy thông tin người dùng:", err);
        setError("Không thể lấy thông tin người dùng.");
        setLoading(false);
      }
    };
    fetchUserId();
  }, [user]);

  // Lấy danh sách thẻ xe
  useEffect(() => {
    const fetchCards = async () => {
      if (!userId) return;

      try {
        const res = await authApis().get(`/card/user/${userId}`);
        setCards(res.data);
        setLoading(false);
      } catch (err) {
        console.error("Lỗi khi tải thẻ xe:", err);
        setError("Không thể tải danh sách thẻ xe.");
        setLoading(false);
      }
    };

    fetchCards();
  }, [userId]);

  const handleInputChange = (e) => {
    setNewCard({ ...newCard, [e.target.name]: e.target.value });
  };

  const handleCreateCard = async (e) => {
    e.preventDefault();
    if (!user) {
      alert("Vui lòng đăng nhập để tạo thẻ xe!");
      navigator("/login", { replace: true });
      return;
    }

    try {
      const formData = new FormData();
      formData.append("holderName", newCard.holderName);
      formData.append("relationship", newCard.relationship);
      formData.append("userId.id", parseInt(userId)); // Đảm bảo userId là số nguyên

      // Debug: In ra FormData
      for (let [key, value] of formData.entries()) {
        console.log(`${key}: ${value}`);
      }

      const res = await authApis().post(endpoints["create-Card"], formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });

      setCards([...cards, res.data]);
      setNewCard({
        holderName: "",
        relationship: "",
      });
      setShowForm(false);
      alert("Tạo thẻ thành công!");
    } catch (err) {
      console.error("Lỗi khi tạo thẻ:", err);
      let errorMessage = "Tạo thẻ không thành công!";
      if (err.response && err.response.data) {
        if (typeof err.response.data === "string" && err.response.data.includes("Apache Tomcat")) {
          errorMessage = "Tạo thẻ không thành công: Yêu cầu không hợp lệ (Bad Request).";
        } else if (err.response.data.message) {
          errorMessage = `Tạo thẻ không thành công: ${err.response.data.message}`;
        }
      } else if (err.response && err.response.status === 401) {
        errorMessage = "Tạo thẻ không thành công: Vui lòng đăng nhập lại.";
        navigator("/login", { replace: true });
      }
      alert(errorMessage);
    }
  };

  const handleDeleteCard = async (cardId) => {
    if (!user) {
      alert("Vui lòng đăng nhập để xóa thẻ xe!");
      navigator("/login", { replace: true });
      return;
    }

    // Xác nhận trước khi xóa
    if (!window.confirm("Bạn có chắc muốn xóa thẻ xe này?")) {
      return;
    }

    try {
      const res = await authApis().delete(endpoints["delete-card"](cardId));
      if (res.status === 200) {
        // Cập nhật danh sách thẻ xe bằng cách loại bỏ thẻ đã xóa
        setCards(cards.filter((card) => card.id !== cardId));
        alert("Xóa thẻ xe thành công!");
      }
    } catch (err) {
      console.error("Lỗi khi xóa thẻ:", err);
      let errorMessage = "Xóa thẻ không thành công!";
      if (err.response) {
        if (err.response.status === 404) {
          errorMessage = "Thẻ xe không tồn tại.";
        } else if (err.response.status === 401) {
          errorMessage = "Vui lòng đăng nhập lại.";
          navigator("/login", { replace: true });
        } else if (err.response.data && err.response.data.message) {
          errorMessage = `Xóa thẻ không thành công: ${err.response.data.message}`;
        }
      }
      alert(errorMessage);
    }
  };

  if (!user) return null; // Không render gì nếu chưa đăng nhập
  if (loading) return <p className="text-blue-500">Đang tải dữ liệu...</p>;
  if (error) return <p className="text-red-500">{error}</p>;

  return (
    <div className="max-w-4xl mx-auto p-4">
      <div className="flex items-center justify-between mb-4">
        <h2 className="text-2xl font-bold">Danh sách thẻ xe của bạn</h2>
        <button
          onClick={() => setShowForm(!showForm)}
          className="bg-blue-500 hover:bg-blue-600 text-black px-4 py-2 rounded"
        >
          {showForm ? "Hủy" : "Tạo thẻ xe"}
        </button>
      </div>

      {showForm && (
        <form onSubmit={handleCreateCard} className="bg-gray-100 p-4 mb-6 rounded shadow">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <input
              type="text"
              name="holderName"
              placeholder="Tên người giữ thẻ"
              value={newCard.holderName}
              onChange={handleInputChange}
              className="p-2 border rounded"
              required
            />
            <input
              type="text"
              name="relationship"
              placeholder="Quan hệ với chủ hộ"
              value={newCard.relationship}
              onChange={handleInputChange}
              className="p-2 border rounded"
              required
            />
          </div>
          <button
            type="submit"
            className="mt-4 bg-green-600 hover:bg-green-700 text-black px-4 py-2 rounded"
          >
            Xác nhận tạo thẻ
          </button>
        </form>
      )}

      {cards.length === 0 ? (
        <p>Không có thẻ nào cho người dùng này.</p>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {cards.map((card) => (
            <div
              key={card.id}
              className="border rounded p-4 shadow hover:shadow-md transition duration-200"
            >
              <h3 className="text-lg font-semibold mb-2">{card.holderName}</h3>
              <p>
                <strong>Quan hệ:</strong> {card.relationship || "Chưa rõ"}
              </p>
              <p>
                <strong>Ngày cấp:</strong>{" "}
                {moment(card.issueDate).format("DD/MM/YYYY")}
              </p>
              <p>
                <strong>Ngày hết hạn:</strong>{" "}
                {moment(card.expirationDate).format("DD/MM/YYYY")}
              </p>
              <p>
                <strong>Trạng thái:</strong>
                <span
                  className={`ml-2 px-2 py-1 rounded text-black text-sm ${
                    card.status === "active" ? "bg-green-500" : "bg-gray-500"
                  }`}
                >
                  {card.status}
                </span>
              </p>
              <button
                onClick={() => handleDeleteCard(card.id)}
                className="mt-2 bg-red-500 hover:bg-red-600 text-black px-4 py-1 rounded"
              >
                Xóa
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Card;