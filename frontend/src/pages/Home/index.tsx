import { ReactComponent as MainImage } from 'assets/images/Desenho.svg'; 
import './styles.css';
import ButtonIcon from "components/ButtonIcon";
import { Link } from 'react-router-dom';

function Home() {
    return (
      <>
        
        <div className="home-container">  
            
            <div className="base-card home-card">

                <div className="home-content-container">

                    <div>
                        <h1>Conheça o melhor catálogo do curso!</h1>
                        <p>Ajudaremos você a encontrar os melhores produtos disponíveis no mercado.</p>
                    </div>

                    <div>
                        <Link to="/products">
                        <   ButtonIcon />
                        </Link>
                        
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