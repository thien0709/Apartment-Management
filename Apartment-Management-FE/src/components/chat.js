import { useContext, useEffect, useState } from "react";
import Apis, { endpoints } from "../configs/Apis";
import { MyUserContext } from "../configs/MyContexts";
import { database } from "../configs/firebase";
import { ref, onValue, push } from "firebase/database";
import { useNavigate } from "react-router-dom";

const Chat = () => {
  const user = useContext(MyUserContext).user;
  const [admins, setAdmins] = useState([]);
  const [selectedAdmin, setSelectedAdmin] = useState(null);
  const [messages, setMessages] = useState([]);
  const [newMsg, setNewMsg] = useState("");
  const navigator = useNavigate();

  useEffect(() => {
    if (!user) navigator("/login");
    const fetchAdmins = async () => {
      try {
        const res = await Apis.get(endpoints["get-admins"]);
        setAdmins(res.data);
        console.log("Admins loaded:", res.data);
      } catch (err) {
        console.error("Lỗi khi tải admin:", err);
      }
    };
    fetchAdmins();
  }, []);

  useEffect(() => {
    if (!selectedAdmin || !user) return;

    const chatId = `${user.id}_${selectedAdmin.id}`;
    const chatRef = ref(database, `chats/${chatId}`);

    const unsubscribe = onValue(chatRef, (snapshot) => {
      const data = snapshot.val() || {};
      const loaded = Object.values(data);
      setMessages(loaded);
    });

    return () => unsubscribe();
  }, [selectedAdmin, user]);

  const handleSend = () => {
    if (!newMsg.trim() || !user || !selectedAdmin) return;

    const chatId = `${user.id}_${selectedAdmin.id}`;
    const chatRef = ref(database, `chats/${chatId}`);

    const msg = {
      senderId: user.id,
      receiverId: selectedAdmin.id,
      content: newMsg,
      timestamp: Date.now(),
    };

    push(chatRef, msg);
    setNewMsg("");
  };

  return (
    <div className="container-fluid">
      <div className="row" style={{ height: "80vh" }}>
        <div
    className="col-md-4 border-end"
    style={{ overflowY: "auto", maxHeight: "80vh" }}
  >
          <h5 className="mt-3">Danh sách Admin</h5>
          <ul className="list-group">
            {admins.map((admin) => (
              <li
                key={admin.id}
                className={`list-group-item list-group-item-action ${
                  selectedAdmin?.id === admin.id ? "active" : ""
                }`}
                style={{ cursor: "pointer" }}
                onClick={() => setSelectedAdmin(admin)}
              >
                {admin.fullName}
              </li>
            ))}
          </ul>
        </div>

          <div className="col-md-8 d-flex flex-column" style={{ height: "80vh" }}>
          <div className="border-bottom py-2 px-3 bg-light">
            <strong>
              Chat với: {selectedAdmin?.fullName || "Chưa chọn admin"}
            </strong>
          </div>

          <div className="flex-grow-1 overflow-auto p-3" >
            {messages.map((msg, idx) => (
              <div
                key={idx}
                className={`mb-2 ${
                  msg.senderId === user.id ? "text-end" : "text-start"
                }`}
              >
                <span
                  className={`d-inline-block p-2 rounded ${
                    msg.senderId === user.id
                      ? "bg-primary text-white"
                      : "bg-light"
                  }`}
                >
                  {msg.content}
                </span>
              </div>
            ))}
          </div>

          {selectedAdmin && (
            <div className="p-3 border-top">
              <div className="input-group">
                <input
                  type="text"
                  className="form-control"
                  placeholder="Nhập tin nhắn..."
                  value={newMsg}
                  onChange={(e) => setNewMsg(e.target.value)}
                  onKeyDown={(e) => e.key === "Enter" && handleSend()}
                />
                <button className="btn btn-primary" onClick={handleSend}>
                  Gửi
                </button>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Chat;
