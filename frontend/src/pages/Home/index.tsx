/* eslint-disable react/jsx-no-comment-textnodes */
import Navbar from "components/Navbar";
import { ReactComponent as MainImage } from 'assets/images/Desenho.svg'; 
import './styles.css';
import ButtonIcon from "components/ButtonIcon";

function Home() {
    return (
      <>
        <Navbar />
        
        <div className="home-container">  
            
            <div className="base-card home-card">

                <div className="home-content-container">

                    <div>
                        <h1>Conheça o melhor catálogo do curso!</h1>
                        <p>Ajudaremos você a encontrar os melhores produtos disponíveis no mercado.</p>
                    </div>

                    <div>
                        <ButtonIcon />
                    </div>
                    
                </div>

                <div className="home-image-container">
                    <MainImage />
                </div>
            </div>
        </div>

      </>
    );
  }
  
  export default Home;