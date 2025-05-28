import React, { useState, useContext, useEffect } from "react";
import { Button, Alert, Form, Image } from "react-bootstrap";
import { authApis } from "../configs/Apis";
import { MyUserContext } from "../configs/MyContexts";
import { useNavigate } from "react-router-dom";

const UploadAvatar = () => {
  const [avatar, setAvatar] = useState(null);         // file mới chọn
  const [preview, setPreview] = useState(null);       // URL preview ảnh mới
  const [msg, setMsg] = useState(null);
  const [variant, setVariant] = useState("success");

  const { user } = useContext(MyUserContext);
  const navigate = useNavigate();

  useEffect(() => {
    // Tạo URL xem trước ảnh khi user chọn file mới
    if (!avatar) {
      setPreview(null);
      return;
    }

    const objectUrl = URL.createObjectURL(avatar);
    setPreview(objectUrl);

    // Cleanup URL khi component unmount hoặc avatar thay đổi
    return () => URL.revokeObjectURL(objectUrl);
  }, [avatar]);

  if (!user) {
    navigate("/login");
    return null;
  }

  const handleChangeAvatar = async (e) => {
    e.preventDefault();

    if (!avatar) {
      setMsg("Vui lòng chọn ảnh.");
      setVariant("warning");
      return;
    }

    const formData = new FormData();
    formData.append("avatar", avatar);

    try {
      const res = await authApis().put(`/users/${user.id}/update_avatar`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });

      setMsg(res.data.message || "Cập nhật avatar thành công!");
      setVariant("success");
      setAvatar(null); 
      setPreview(null);
      navigate("/change-password");
    } catch (err) {
      console.error("Upload error:", err);
      setMsg(err.response?.data?.error || "Cập nhật thất bại.");
      setVariant("danger");
    }
  };

  return (
    <Form onSubmit={handleChangeAvatar} style={{ maxWidth: "400px", margin: "auto" }}>
      {msg && <Alert variant={variant}>{msg}</Alert>}

      <div className="mb-3 text-center">
        {/* Hiển thị ảnh hiện tại hoặc ảnh preview */}
        {preview ? (
          <Image
            src={preview}
            roundedCircle
            fluid
            alt="Ảnh đại diện mới"
            style={{ width: "150px", height: "150px", objectFit: "cover" }}
          />
        ) : user.avatarUrl ? (
          <Image
            src={user.avatarUrl}
            roundedCircle
            fluid
            alt="Ảnh đại diện hiện tại"
            style={{ width: "150px", height: "150px", objectFit: "cover" }}
          />
        ) : (
          <div
            style={{
              width: "150px",
              height: "150px",
              borderRadius: "50%",
              backgroundColor: "#ddd",
              lineHeight: "150px",
              textAlign: "center",
              color: "#666",
              margin: "0 auto",
            }}
          >
            Chưa có ảnh
          </div>
        )}
      </div>

      <Form.Group className="mb-3">
        <Form.Label>Chọn ảnh đại diện mới</Form.Label>
        <Form.Control
          type="file"
          accept="image/*"
          onChange={(e) => setAvatar(e.target.files[0])}
        />
      </Form.Group>

      <Button type="submit" disabled={!avatar}>
        Cập nhật avatar
      </Button>
    </Form>
  );
};

export default UploadAvatar;
