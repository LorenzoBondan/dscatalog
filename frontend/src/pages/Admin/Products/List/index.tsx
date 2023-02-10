import { AxiosRequestConfig } from 'axios';
import ProductCrudCard from 'pages/Admin/Products/ProductCrudCard';
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Product } from 'types/product';
import { SpringPage } from 'types/vendor/spring';
import { requestBackend } from 'util/requests';

import './styles.css';

const List = () => {

  const [page, setPage] = useState<SpringPage<Product>>();

  useEffect(() => {
    
    const params : AxiosRequestConfig = {
      method:"GET",
      url: "/products",
      params: {
        page: 0,
        size: 50
      },
    }

    requestBackend(params) // função criada no requests.ts
      .then(response => {
        setPage(response.data);
      });
  }, []);


  return (
    <div className="product-crud-container">

      <div className="product-crud-bar-container">

        <Link to="/admin/products/create">
          <button className="btn btn-primary text-white btn-crud-add">
            ADICIONAR
          </button>
        </Link>

        <div className="base-card product-filter-container">SearchBar</div>

      </div>

      <div className="row">

        <div className="col-sm-6 col-md-12">
          {page?.content.map(product => (
            <ProductCrudCard product={product} key={product.id}/>
          ))}
        </div>


        
      </div>
    </div>
  );
};

export default List;
