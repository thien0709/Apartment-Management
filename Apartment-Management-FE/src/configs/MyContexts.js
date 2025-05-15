import { createContext, useReducer } from "react";

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

  return (
    <MyDispatcherContext.Provider value={dispatch}>
      <MyUserContext.Provider value={state}>
        {children}
      </MyUserContext.Provider>
    </MyDispatcherContext.Provider>
  );
};