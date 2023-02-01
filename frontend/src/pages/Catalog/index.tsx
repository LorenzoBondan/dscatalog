
import { AxiosRequestConfig } from "axios";
import Pagination from "components/Pagination";
import ProductCard from "components/ProductCard";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Product } from "types/product";
import { SpringPage } from "types/vendor/spring";
import { requestBackend } from "util/requests";
import CardLoader from "./CardLoader";
import './styles.css';


function Catalog() {

  const [page, setPage] = useState<SpringPage<Product>>();
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    
    const params : AxiosRequestConfig = {
      method:"GET",
      url: "/products",
      params: {
        page: 0,
        size: 12
      },
    }

    setIsLoading(true); // antes da requisição, está carregando
    requestBackend(params) // função criada no requests.ts
      .then(response => {
        setPage(response.data);
      })
      .finally(() => {
        setIsLoading(false); // terminou a requisição, isLoading = false
      });
  }, []);

// percorrer os elementos da page preenchendo os product cards

    return (
      <>
        <div className="container my-4 catalog-container">

          <div className="row catalog-title-container">
            <h1>Catálogo de produtos</h1>
          </div>

            <div className="row">
              
              {isLoading ? <CardLoader /> : ( // se o isLoading for verdadeiro, carregando, se for falso, o restante
                page?.content.map(product => (
                  <div className="col-sm-6 col-lg-4 col-xl-3" key={product.id}>
                    <Link to="/products/1">
                      <ProductCard product={product} />
                    </Link>
                  </div>
                )
              ))}

            </div>

            <div className="row">
              <Pagination />
            </div>
        </div>
        
      </>
    );
  }

  export default Catalog;