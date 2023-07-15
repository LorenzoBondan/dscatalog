import { AxiosRequestConfig } from 'axios';
import Pagination from 'components/Pagination';
import ProductFilter, { ProductFilterData } from 'components/ProductFilter';
import ProductCrudCard from 'pages/Admin/Products/ProductCrudCard';
import { useCallback, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Product } from 'types/product';
import { SpringPage } from 'types/vendor/spring';
import { requestBackend } from 'util/requests';
import './styles.css';

type ControlComponentsData = {
  activePage: number;
  filterData: ProductFilterData;
}

const List = () => {

  const [page, setPage] = useState<SpringPage<Product>>();

  //mater o estado de todos os componentes que fazem a listagem
  const [controlComponentsData, setControlComponentsData] = useState<ControlComponentsData>({activePage:0, filterData: { name: '', category: null },});

  const handlePageChange = (pageNumber : number) => {
    setControlComponentsData({activePage: pageNumber, filterData: controlComponentsData.filterData});
    //mantém o que está no filtro e muda só a página
  }

  const getProducts = useCallback(() => {
    const params : AxiosRequestConfig = {
      method:"GET",
      url: "/products",
      params: {
        page: controlComponentsData.activePage,
        size: 3,
        
        name: controlComponentsData.filterData.name,
        categoryId:  controlComponentsData.filterData.category?.id
      },
    }

    requestBackend(params) // função criada no requests.ts
      .then(response => {
        setPage(response.data);
      });
  }, [controlComponentsData])

  useEffect(() => {
    getProducts();
  }, [getProducts]);

// função do componente ProductFilter
 const handleSubmitFilter = (data : ProductFilterData) => {
  setControlComponentsData({activePage: 0, filterData: data});
  // efetua o filtro e volta pra primeira página
 }

  return (
    <div className="product-crud-container">
      <div className="product-crud-bar-container">
        <Link to="/admin/products/create">
          <button className="btn btn-primary text-white btn-crud-add">
            ADICIONAR
          </button>
        </Link>
        <ProductFilter onSubmitFilter={handleSubmitFilter} />
      </div>
      <div className="row">
          {page?.content.map(product => (
            <div className="col-sm-6 col-md-12" key={product.id}>
              <ProductCrudCard product={product} onDelete={() => getProducts()} />
            </div>
          ))}
      </div>
      <Pagination 
        pageCount = {(page) ? page.totalPages : 0} 
        range = {3}
        onChange = {handlePageChange}
        forcePage={page?.number}
      />
    </div>
  );
};

export default List;

