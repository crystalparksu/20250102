/* 로그아웃 컴포넌트 */

//기본
import axios from "axios";
import {useEffect, useContext} from "react";
import { useNavigate } from "react-router";

//인증
import { AuthContext } from "../context/AuthProvider";


//---------------------
function Logout(){
    //로그아웃을 위한 설정
    const[auth, setAuth]  = useContext(AuthContext);

    //이동
    const useNavigate = useNavigate();


    //로그아웃 함수
    const logout = () => {
        localStorage.removeItem("bbs_access_token");
        localStorage.removeItem("id");

        alert( auth + "님 성공적으로 로그아웃 됐습니다 🔒");
        setAuth(null);
        navigate("/");
    };

    //로그아웃 함수 호출
    useEffect(() => {
        logout();
    }, []);

}
export default Logout;

