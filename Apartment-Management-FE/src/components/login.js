// src/components/login.js
import { useContext, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "./styles/login.css";
import { FaGithub, FaGoogle } from "react-icons/fa";
import { Alert, Spinner } from "react-bootstrap";
import Apis, { endpoints } from "../configs/Apis";
import { MyDispatcherContext } from "../configs/MyContexts";

const Login = () => {
  const [user, setUser] = useState({ username: "", password: "" });
  const [msg, setMsg] = useState(null);
  const [loading, setLoading] = useState(false);
  const nav = useNavigate();
  const dispatch = useContext(MyDispatcherContext);

  const handleChange = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };

  const login = async (e) => {
    e.preventDefault();
    setMsg(null);
    setLoading(true);

    try {
      const form = new FormData();
      form.append("username", user.username);
      form.append("password", user.password);

      const res = await Apis.post(endpoints.login, form, {
        headers: {
          Accept: "application/json",
        },
      });

      if (typeof dispatch !== "function") {
        throw new Error("dispatch is not a function");
      }

      dispatch({
        type: "login",
        payload: res.data,
      });

      nav("/");
    } catch (err) {
      console.error("Error:", err);
      setMsg(err.response?.data || "Tên đăng nhập hoặc mật khẩu không đúng!");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <form className="glass-form" onSubmit={login}>
        <h3>Đăng nhập</h3>

        {msg && <Alert variant="danger">{msg}</Alert>}

        <label htmlFor="username">Tên đăng nhập</label>
        <input
          type="text"
          id="username"
          name="username"
          value={user.username}
          onChange={handleChange}
          required
        />

        <label htmlFor="password">Mật khẩu</label>
        <input
          type="password"
          id="password"
          name="password"
          value={user.password}
          onChange={handleChange}
          required
        />

        <button type="submit" disabled={loading}>
          {loading ? <Spinner animation="border" size="sm" /> : "Đăng nhập"}
        </button>

        <div className="social">
          <Link to="/auth/google" className="go">
            <FaGoogle className="icon" /> Google
          </Link>
          <Link to="/auth/github" className="go">
            <FaGithub className="icon" /> GitHub
          </Link>
        </div>
      </form>
    </div>
  );
};

export default Login;
