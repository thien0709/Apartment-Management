import { useContext } from "react";
import { Link, useNavigate } from "react-router-dom";
import { MyUserContext, MyDispatcherContext } from "../../configs/MyContexts"; // thêm Dispatcher
import cookie from "react-cookies";

import Container from "react-bootstrap/Container";
import Nav from "react-bootstrap/Nav";
import Navbar from "react-bootstrap/Navbar";
import Offcanvas from "react-bootstrap/Offcanvas";

function Sidebar() {
  const { user } = useContext(MyUserContext);
  const dispatch = useContext(MyDispatcherContext);
  const navigate = useNavigate();

  const logout = () => {
    // Xóa token trong cookie
    cookie.remove("token");

    // Cập nhật context
    dispatch({
      type: "logout",
    });

    // Điều hướng về trang đăng nhập hoặc trang chủ
    navigate("/login");
  };

  return (
    <Navbar expand="lg" className="bg-body-tertiary mb-3">
      <Container fluid>
        <Navbar.Brand href="#">Quản Lý Căn Hộ</Navbar.Brand>
        <Navbar.Toggle aria-controls={`offcanvasNavbar-expand-lg`} />

        <Navbar.Offcanvas
          id={`offcanvasNavbar-expand-lg`}
          aria-labelledby={`offcanvasNavbarLabel-expand-lg`}
          placement="end"
        >
          <Offcanvas.Header closeButton>
            <Offcanvas.Title id={`offcanvasNavbarLabel-expand-lg`}>
              Chức năng
            </Offcanvas.Title>
          </Offcanvas.Header>
          <Offcanvas.Body>
            <Nav className="justify-content-end flex-grow-1 pe-3">
              <Link to="/">Trang chủ</Link>
              <Link to="/payment">Thanh toán</Link>

              {user ? (
                <>
                  <span
                    style={{ marginLeft: "1rem", fontWeight: "bold", color: "#0d6efd" }}
                  >
                    Xin chào, {user.username}
                  </span>
                  <button
                    onClick={logout}
                    className="btn btn-outline-danger ms-3"
                  >
                    Đăng xuất
                  </button>
                </>
              ) : (
                <Link to="/login">Đăng nhập</Link>
              )}
            </Nav>
          </Offcanvas.Body>
        </Navbar.Offcanvas>
      </Container>
    </Navbar>
  );
}

export default Sidebar;
