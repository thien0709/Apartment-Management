import { useState, useEffect, useContext} from "react";
import { Link, useNavigate } from "react-router-dom";
import Apis, { endpoints } from "../configs/Apis";
import { MyUserContext } from "../configs/MyContexts";

const SurveyList = () => {
  const [surveys, setSurveys] = useState([]);
  const user = useContext(MyUserContext);
  const navigator = useNavigate();
  const loadSurveys = async () => {
    try {
      const res = await Apis.get(endpoints["surveys"]);
      setSurveys(res.data);
    } catch (error) {
      console.error("Lỗi tải danh sách khảo sát:", error);
    }
  } ;


    useEffect(() => {
    if (!user.user?.id) navigator("/login");
   loadSurveys();
  }, [user.user?.id]);

  return (
    <div className="container my-5">
      <h2 className="mb-4 text-primary fw-bold">Danh sách khảo sát</h2>
      {surveys.length === 0 ? (
        <p className="text-muted fst-italic">Chưa có khảo sát nào.</p>
      ) : (
        surveys.map((survey) => (
          <div key={survey.id} className="card mb-4 border rounded-3 shadow-sm">
            <div className="card-body">
              <h5 className="card-title text-primary fw-bold">{survey.title}</h5>
              <p className="card-text text-secondary">{survey.description}</p>
              <Link
                to={`/survey/${survey.id}`}
                className="btn btn-outline-primary mt-3"
              >
                Tham gia khảo sát
              </Link>
            </div>
          </div>
        ))
      )}
    </div>
  );
};

export default SurveyList;
