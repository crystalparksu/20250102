import {BrowserRouter} from 'react-router-dom';

//Components
import Header from './Components/app/Header';
import Main from './Components/app/Main';
import Footer from './Components/app/Footer';


//css
import "./css/style.css"
import "./css/main.css"


//context
import AuthProvider from "./Components/context/AuthProvider";
import HttpHeadersProvider from "./Components/context/HttpHeadersProvider";


//App
function App() {
  return (
      <div>
          <BrowserRouter>
              <AuthProvider>
                  <HttpHeadersProvider>
                      <Header/>
                      <Main/>
                      <Footer/>
                  </HttpHeadersProvider>
              </AuthProvider>
          </BrowserRouter>
      </div>
  );
}
export default App;
