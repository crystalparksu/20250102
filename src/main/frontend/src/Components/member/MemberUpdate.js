/* 회원정보수정 컴포넌트 */

//기본
import axios from "axios";
import {useState, useContext, useEffect} from "react";
import { useNavigate } from "react-router";

//인증
import { AuthContext } from "../context/AuthProvider";
import { HttpHeadersContext } from "../context/HttpHeadersProvider";


//------------------
function MemberUpdate(props){
    //설정
    const {headers, setHeaders} = useContext(HttpHeadersContext);

    //이름,패스워드,패스워드체크
    const [name, setName] = useState("");
    const [pwd, setPwd] = useState("");
    const [checkPwd, setCheckPwd] = useState("");


    //이메일은 부모 컴포넌트 받아 확인
    const email = props.email;

    //이동
    const navigate = useNavigate();

    //이벤트
    const ChangeName = (event) => {
        setName(event.target.value);
    }
    const ChangePwd = (event) => {
        setPwd(event.target.value);
    }
    const ChangeCheckPwd = (event) => {
        setCheckPwd(event.target.value);
    }

    //함수 호출: 회원정보 수정 시,
    //새로고침하면 App Context 사라지기 때문에, 초기 값은 LocalStorage 값으로 세팅
    useEffect(() => {
        setHeaders({
            Authorization: `Bearer ${localStorage.getItem("bbs_access_token")}`,
        });
        setName(props.name);
    }, [props.name]);


    //REST API - 회원정보 수정
    const  update = async () => {

        const req = {
            password: pwd,
            passwordCheck: checkPwd,
            username: name,
        };


        await axios
            .post("http://localhost:8080/user/update", req, { headers: headers })
            .then((resp) =>{
                console.log(resp.data);

                alert(resp.data.username + "님의 회원정보를 수정했습니다.");
                navigate("/");
            })
            .catch((err)=>{
                console.log(err);

                const resp = err.response;
                if(resp.status === 400){
                    alert(resp.data);
                }
            });
    };














    //HTML
    return (
        <div>
            <table className="table">
                <tbody>
                <tr>
                    <th>이메일</th>
                    <td>
                        <input
                            type="text"
                            className="form-control"
                            value={email}
                            size="50px"
                            readOnly
                        />
                    </td>
                </tr>

                <tr>
                    <th>이름</th>
                    <td>
                        <input
                            type="text"
                            value={name}
                            onChange={changeName}
                            size="50px"
                        />
                    </td>
                </tr>

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

                <tr>
                    <th>비밀번호 확인</th>
                    <td>
                        <input
                            type="password"
                            value={checkPwd}
                            onChange={changeCheckPwd}
                            size="50px"
                        />
                    </td>
                </tr>
                </tbody>
            </table>
            <br />

            <div className="my-3 d-flex justify-content-center">
                <button className="btn btn-outline-secondary" onClick={update}>
                    <i className="fas fa-user-plus"></i>정보 수정
                </button>
            </div>
        </div>
    );
}
export default MemberUpdate;