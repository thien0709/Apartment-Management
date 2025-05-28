import { BrowserRouter, Route, Routes } from "react-router-dom";
import "./App.css";
import Home from "./components/home";
import Sidebar from "./components/layout/sidebar";
import "bootstrap/dist/css/bootstrap.min.css";
import Footer from "./components/layout/footer";
import Login from "./components/login";
import Payment from "./components/payment";
import { AuthProvider, MyDispatcherContext } from "./configs/MyContexts";
import ChangePassword from "./components/changePassword";
import UploadAvatar from "./components/uploadAvatar";
import Feedback from "./components/feedback";
import { useContext, useEffect } from "react";
import cookie from "react-cookies";
import Apis, { endpoints } from "./configs/Apis";
import SurveyDetail from "./components/surveyDetail";
import SurveyList from "./components/surveyList";
import Chat from "./components/chat";
import Card from './components/card';

function App() {
  return (
    <AuthProvider>
      <AppRoutes />
    </AuthProvider>
  );
}

function AppRoutes() {
  const dispatch = useContext(MyDispatcherContext);

  useEffect(() => {
    const autoLogin = async () => {
      const token = cookie.load("token");
      if (token) {
        try {
          const res = await Apis.get(endpoints["current-user"], {
            headers: { Authorization: `Bearer ${token}` },
          });
          dispatch({ type: "login", payload: res.data });
        } catch (err) {
          cookie.remove("token");
          console.error("Auto login failed:", err);
        }
      }
    };
    autoLogin();
  }, [dispatch]);

  return (
    <BrowserRouter>
      <Sidebar />
      <div className="content-wrap">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/payment" element={<Payment />} />
          <Route path="/feedback" element={<Feedback />} />
          <Route path="/change-password" element={<ChangePassword />} />
          <Route path="/update-avatar" element={<UploadAvatar />} />
            <Route path='/card' element={<Card/>} />
          <Route path="/survey" element={<SurveyList />} />
          <Route path="/survey/:surveyId" element={<SurveyDetail />} />
          <Route path="/chats" element={<Chat />} />
        </Routes>
      </div>
      <Footer />
    </BrowserRouter>
  );
}

export default App;
