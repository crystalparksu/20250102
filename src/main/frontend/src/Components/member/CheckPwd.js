/* 회원정보(패스워드 체크) 컴포넌트 */

//기본
import axios from "axios";
import {useState, useContext, useEffect} from "react";
import { useNavigate } from "react-router";

//인증
import { AuthContext } from "../context/AuthProvider";
import { HttpHeadersContext } from "../context/HttpHeadersProvider";

//회원정보 수정 컴포넌트
import MemberUpdate from "./MemberUpdate"


// ------------------------
function CheckPwd(){
    //설정
    const {headers, setHeaders} = useContext(HttpHeadersContext);

    //이메일,이름,패스워드
    const [email, setEmail] = useState("");
    const [name, setName] = useState("");
    const [pwd, setPwd] = useState("");

    //멤버) 회원정보 수정
    const [showMemberUpdate, setShowMemberUpdate] = useState(false);

    //이벤트
    const ChangeEamil = (event) => {
        setEmail(event.target.value);
    }
    const ChangeName = (event) => {
        setName(event.target.value);
    }
    const ChangePwd = (event) => {
        setPwd(event.target.value);
    }

    //함수 호출: 컴포넌트가 렌더링될 때마다 localStorage의 토큰 값으로 headers를 업데이트
    useEffect(() => {
        setHeaders({
            Authorization: `Bearer ${localStorage.getItem("bbs_access_token")}`,
        });
    }, []);


    //REST API - 비밀번호 체크
    const  passwordCheck = async () => {

        const req = {
            password: pwd,
        };

        try{
            const resp = await axios
                .post("http://localhost:8080/user/checkPwd",
                    req,
                    { headers: headers }
                );
            console.log(resp.data);
            setEmail(resp.data.email);
            setName(resp.data.username);
            setShowMemberUpdate(true);
        }catch (err) {
            console.log(err);

            const resp = err.response;
            if(resp.status === 400){
                alert(resp.data);
            }
        }
    };




    //HTML
    return (
        <div>
            {showMemberUpdate ? (
                <MemberUpdate email={email} name={name} />
            ) : (
                <>
                    <table className="table">
                        <tbody>
                        <tr>
                            <th>비밀번호</th>
                            <td>
                                <input
                                    type="password"
                                    value={pwd}
                                    onChange={changePwd}
                                    size="50px"
                                />
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    <br />

                    <div className="my-3 d-flex justify-content-center">
                        <button
                            className="btn btn-outline-secondary"
                            onClick={passwordCheck}
                        >
                            <i className="fas fa-user-plus"></i>비밀번호 확인
                        </button>
                    </div>
                </>
            )}
        </div>
    );
}
export default CheckPwd;
