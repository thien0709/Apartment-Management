import { useEffect, useState, useContext } from "react";
import { MyUserContext } from "../configs/MyContexts";
import Apis, { endpoints } from "../configs/Apis";

const Feedback = () => {
  const user = useContext(MyUserContext);
  const [content, setContent] = useState("");
  const [feedbacks, setFeedbacks] = useState([]);
  const [editingId, setEditingId] = useState(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const loadFeedbacks = async () => {
    try {
      const res = await Apis.get(endpoints["feedbacks"](user.user?.id), {
        headers: {
          Authorization: `Bearer ${user.token}`,
        },
      });
      console.log("Phản ánh:", res.data);
      setFeedbacks(res.data);
    } catch (error) {
      console.error("Lỗi tải phản ánh:", error);
    }
  };

  useEffect(() => {
    if (user.user?.id) loadFeedbacks();
  }, [user.user?.id]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!content.trim()) {
      alert("Vui lòng nhập nội dung phản ánh.");
      return;
    }

    setIsSubmitting(true);

    try {
      if (editingId) {
        // Cập nhật phản ánh
        const res = await Apis.put(`${endpoints["feedback"]}/${editingId}`, {
          content,
        });
        if (res.status === 200) {
          alert("Đã cập nhật phản ánh.");
          setEditingId(null);
        }
      } else {
        // Tạo phản ánh mới
        const res = await Apis.post(endpoints["feedback"], {
          userId: user.user.id,
          content,
        });
        if (res.status === 201) {
          alert("Phản ánh của bạn đã được gửi thành công!");
        }
      }

      setContent("");
      await loadFeedbacks();
    } catch (error) {
      console.error("Lỗi gửi/cập nhật phản ánh:", error);
      alert("Đã có lỗi xảy ra. Vui lòng thử lại.");
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleEdit = (fb) => {
    setContent(fb.content);
    setEditingId(fb.id);
  };

  const handleDelete = async (id) => {
    if (window.confirm("Bạn có chắc muốn xóa phản ánh này không?")) {
      try {
        const res = await Apis.delete(`${endpoints["feedback"]}/${id}`);
        if (res.status === 204) {
          alert("Đã xóa phản ánh.");
          await loadFeedbacks();
        }
      } catch (err) {
        console.error("Lỗi xóa phản ánh:", err);
        alert("Không thể xóa phản ánh.");
      }
    }
  };

  return (
    <div className="container py-5">
      <div className="row">
        {/* Form gửi phản ánh */}
        <div className="col-md-6 mb-4">
          <div className="card shadow border-0 rounded-4">
            <div className="card-body p-4">
              <h4 className="card-title text-center text-primary">
                {editingId ? "Chỉnh sửa phản ánh" : "Gửi phản ánh"}
              </h4>
              <form onSubmit={handleSubmit}>
                <div className="form-floating mb-3">
                  <textarea
                    className="form-control"
                    rows="5"
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                    placeholder="Nội dung phản ánh"
                    style={{ height: "150px" }}
                    required
                  ></textarea>
                  <label>Nội dung phản ánh</label>
                </div>
                <div className="d-grid">
                  <button
                    type="submit"
                    className="btn btn-primary btn-lg rounded-3"
                    disabled={isSubmitting}
                  >
                    {isSubmitting
                      ? "Đang gửi..."
                      : editingId
                      ? "Cập nhật"
                      : "Gửi phản ánh"}
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>

        {/* Danh sách phản ánh */}
        <div className="col-md-6">
          <h5 className="mb-3 text-primary">Phản ánh của bạn</h5>
          {feedbacks.length === 0 ? (
            <p className="text-muted">Chưa có phản ánh nào.</p>
          ) : (
            <ul className="list-group">
              {feedbacks.map((fb) => (
                <li key={fb.id} className="list-group-item d-flex justify-content-between align-items-start">
                  <div className="flex-grow-1">
                    <p className="mb-1">{fb.content}</p>
                    <small className="text-muted">Ngày gửi: {new Date(fb.createdAt).toLocaleString()}</small>
                  </div>
                  <div className="btn-group btn-group-sm">
                    <button onClick={() => handleEdit(fb)} className="btn btn-outline-secondary">Sửa</button>
                    <button onClick={() => handleDelete(fb.id)} className="btn btn-outline-danger">Xoá</button>
                  </div>
                </li>
              ))}
            </ul>
          )}
        </div>
      </div>
    </div>
  );
};

export default Feedback;
