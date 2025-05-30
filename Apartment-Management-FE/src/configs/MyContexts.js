import { createContext, useEffect, useReducer, useState } from "react";
import cookie from "react-cookies";
import Apis, { endpoints } from "./Apis";

export const MyUserContext = createContext();
export const MyDispatcherContext = createContext();

const reducer = (state, action) => {
  switch (action.type) {
    case "login":
      return { ...state, user: action.payload };
    case "logout":
      return { ...state, user: null };
    default:
      return state;
  }
};
export const AuthProvider = ({ children }) => {
  const [state, dispatch] = useReducer(reducer, { user: null });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const autoLogin = async () => {
      const token = cookie.load("token");
      if (token) {
        try {
          const res = await Apis.get(endpoints["current-user"], {
            params: { role: "RESIDENTS" },
            headers: { Authorization: `Bearer ${token}` },
          });
          dispatch({ type: "login", payload: res.data });
        } catch (err) {
          cookie.remove("token");
        }
      }
      setLoading(false);
    };
    autoLogin();
  }, []);

  return (
    <MyDispatcherContext.Provider value={dispatch}>
      <MyUserContext.Provider value={{ ...state, loading }}>
        {children}
      </MyUserContext.Provider>
    </MyDispatcherContext.Provider>
  );
};
