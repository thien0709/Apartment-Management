import { useContext, useEffect, useState } from "react";
import Apis, { endpoints } from "../configs/Apis";
import { MyUserContext } from "../configs/MyContexts";
import { useParams } from "react-router-dom";

const SurveyDetail = () => {
  const { surveyId } = useParams();
  const [questions, setQuestions] = useState([]);
  const user = useContext(MyUserContext);

  const getQuestions = async () => {
    try {
      const res = await Apis.get(endpoints["survey-detail"](surveyId));
      const question = res.data.map((q) => ({
        ...q,
      }));
      setQuestions(question);
    } catch (err) {
      console.error("Lỗi khi tải khảo sát:", err);
    }
  };

  useEffect(() => {
    getQuestions();
  }, [surveyId]);

  const onChangeAnswer = (questionId, answer) => {
    setQuestions((prev) =>
      prev.map((q) => (q.id === questionId ? { ...q, answer } : q))
    );
  };

  const onSubmit = async (e) => {
    e.preventDefault();

    const payload = questions.map((q) => ({
      questionId: q.id,
      userId: user?.user.id,
      answer: q.answer || "",
    }));

    try {
      await Apis.post(endpoints["survey-detail"](surveyId), payload);
      alert("Cảm ơn bạn đã tham gia khảo sát!");
      setQuestions([]);
    } catch (err) {
      console.error("Lỗi khi gửi khảo sát:", err);
      alert("Đã có lỗi xảy ra, vui lòng thử lại sau.");
    }
  };

  return (
    <div className="container my-5">
      <h2 className="mb-4 text-primary fw-bold">Khảo sát</h2>

      <form onSubmit={onSubmit}>
        {questions.length === 0 && (
          <p className="text-muted">Không có câu hỏi nào cho khảo sát này.</p>
        )}

        {questions.map((question, index) => (
          <div
            key={question.id}
            className="mb-4 p-4 bg-light rounded shadow-sm border"
          >
            <label
              htmlFor={`answer-${question.id}`}
              className="form-label fw-semibold"
            >
              {index + 1}. {question.content}
            </label>
            <textarea
              id={`answer-${question.id}`}
              className="form-control"
              rows={4}
              placeholder="Nhập câu trả lời..."
              value={question.answer || ""}
              onChange={(e) => onChangeAnswer(question.id, e.target.value)}
              required
            />
          </div>
        ))}

        {questions.length > 0 && (
          <button type="submit" className="btn btn-primary w-100 py-2">
            Gửi khảo sát
          </button>
        )}
      </form>
    </div>
  );
};

export default SurveyDetail;
