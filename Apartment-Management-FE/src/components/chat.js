import { useContext, useEffect, useState } from "react";
import Apis, { endpoints } from "../configs/Apis";
import { MyUserContext } from "../configs/MyContexts";

const Chat = ({ onSelect }) => {
  const user = useContext(MyUserContext);
  const [admins, setAdmins] = useState([]);
  const apikey = process.env.REACT_APP_APIKey_Firebase;
  console.log("API Key:", apikey);
  const fetchAdmin = async () => {
    try {
      const res = await Apis.get(endpoints["admins"], {
        headers: {
          Authorization: `Bearer ${user.user.token}`,
        },
      });
      console.log("Admin:", res.data);
      setAdmins(res.data);
    } catch (error) {
      console.error("Lỗi tải phản ánh:", error);
    }
  };

  useEffect(() => {
    console.log("User:", user);
    fetchAdmin();
  }, []);

  return (
    <div className="mb-4">
      <h5 className="text-primary mb-3">Chọn Admin để bắt đầu chat</h5>
      <div className="row">
        {admins.length === 0 && (
          <div className="col-12 text-muted">Không có admin nào.</div>
        )}
        {admins.map((admin) => (
          <div key={admin.id} className="col-md-4 mb-3">
            <div className="card shadow-sm h-100">
              <div className="card-body d-flex flex-column align-items-center">
                <div
                  className="avatar bg-secondary text-white rounded-circle d-flex align-items-center justify-content-center mb-2"
                  style={{ width: "60px", height: "60px", fontSize: "24px" }}
                >
                  {admin.name?.charAt(0).toUpperCase()}
                </div>
                <h6 className="card-title text-center">{admin.name}</h6>
                <button
                  className="btn btn-outline-primary btn-sm mt-2"
                  onClick={() => onSelect(admin.id)}
                >
                  Chat ngay
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Chat;
