// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getDatabase } from "firebase/database";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const APIKey_Firebase = process.env.REACT_APP_APIKey_Firebase;

// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
console.log(APIKey_Firebase);
const firebaseConfig = {
  apiKey: APIKey_Firebase,
  authDomain: "apartment-management-aff89.firebaseapp.com",
  databaseURL: "https://apartment-management-aff89-default-rtdb.asia-southeast1.firebasedatabase.app",
  projectId: "apartment-management-aff89",
  storageBucket: "apartment-management-aff89.firebasestorage.app",
  messagingSenderId: "227248654795",
  appId: "1:227248654795:web:d54b6591f31fee8ac38e4e",
  measurementId: "G-EPN8VZ6N6K"
};

const app = initializeApp(firebaseConfig);
const database = getDatabase(app);

export { database };
