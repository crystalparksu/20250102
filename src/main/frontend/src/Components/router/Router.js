import {Routes, Route} from "react-router-dom";

//app
import Home from "../app/Home"

//bbs
import BbsList from "../bbs/BbsList"
import BbsWrite from "../bbs/BbsWrite"
import BbsDetail from "../bbs/BbsDetail"
import BbsUpdate from "../bbs/BbsUpdate"
import BbsAnswer from "../bbs/BbsAnswer"

//member
import Join from "../member/Join"
import Login from "../member/Login"
import Logout from "../member/Logout"
import MemberUpdate from "../member/MemberUpdate"
import CheckPwd from "../member/CheckPwd";



//Router
function  Router(){
    return(
        <Routes>
            <Route path="/" element={<Home />}></Route>

            {/*게시판*/}
            <Route path="/bbslist" element={<BbsList />}></Route>
            <Route path="/bbswrite" element={<BbsWrite />}></Route>
            <Route path="/bbsdetail/:boardId" element={<BbsDetail />}></Route>
            <Route path="/bbsupdate" element={<BbsUpdate />}></Route>
            <Route path="/bbsanswer/:parentSeq" element={<BbsAnswer />}></Route>

            {/*  멤버  */}
            <Route path="/login" element={<Login />}></Route>
            <Route path="/join" element={<Join />}></Route>
            <Route path="/checkpwd" element={<CheckPwd />}></Route>
            <Route path="/update" element={<MemberUpdate />}></Route>
            <Route path="/logout" element={<Logout />}></Route>
        </Routes>
    );
}
export  default Router;
