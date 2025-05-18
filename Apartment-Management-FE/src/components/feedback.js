import { useState, useContext } from "react";
import { MyUserContext } from "../configs/MyContexts";
import Apis, { endpoints } from "../configs/Apis";

const Feedback = () => {
  const user = useContext(MyUserContext);
  const [content, setContent] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!content.trim()) {
      alert("Vui lòng nhập nội dung phản ánh.");
      return;
    }

    setIsSubmitting(true);

    try {
      const response = await Apis.post(endpoints["feedback"], {
        userId: user.user.id,
        content: content,
      });

      if (response.status === 201) {
        alert("Phản ánh của bạn đã được gửi thành công!");
        setContent("");
      } else {
        alert("Đã có lỗi xảy ra. Vui lòng thử lại.");
      }
    } catch (error) {
      console.error("Lỗi gửi phản ánh:", error);
      alert("Đã có lỗi xảy ra. Vui lòng thử lại.");
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="container py-5">
      <div className="row justify-content-center">
        <div className="col-md-8 col-lg-6">
          <div className="card shadow-lg border-0 rounded-4">
            <div className="card-body p-4">
              <h3 className="card-title text-center mb-3 text-primary">Gửi phản ánh</h3>
              <p className="text-center text-muted mb-4">
                Chúng tôi luôn lắng nghe ý kiến của bạn!
              </p>
              <form onSubmit={handleSubmit}>
                <div className="form-floating mb-4">
                  <textarea
                    id="content"
                    className="form-control"
                    rows="5"
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                    placeholder="Nội dung phản ánh"
                    style={{ height: "150px" }}
                    required
                  ></textarea>
                  <label htmlFor="content">Nội dung phản ánh</label>
                </div>
                <div className="d-grid">
                  <button
                    type="submit"
                    className="btn btn-primary btn-lg rounded-3"
                    disabled={isSubmitting}
                  >
                    {isSubmitting ? "Đang gửi..." : "Gửi phản ánh"}
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Feedback;
