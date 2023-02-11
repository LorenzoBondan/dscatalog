import {ReactComponent as ArrowIcon} from 'assets/images/Seta.svg';
import ReactPaginate from 'react-paginate';
import './styles.css';

const Pagination = () => {
    return(
        <>
        <ReactPaginate 
            pageCount = {10} // a qte de páginas total
            pageRangeDisplayed = {3} // quantas bolinhas da paginação irão aparecer no meio da paginação
            marginPagesDisplayed = {1} // a bolinha que aparece no final
        
            //estilização

            containerClassName= 'pagination-container'
            pageLinkClassName='pagination-item' // bolinhas
            breakClassName='pagination-item' // ...

            previousClassName='arrow-previous'
            nextClassName='arrow-next'

            previousLabel={<ArrowIcon/>} // anterior
            nextLabel={<ArrowIcon/>} // próximo

            activeLinkClassName='pagination-link-active'
            disabledClassName='arrow-inactive'

            //renderiza um ul 
        />

        <div className='pagination-container'>
            <ArrowIcon className='arrow-previous arrow-inactive'/>
            <div className='pagination-item active'>1</div>
            <div className='pagination-item'>2</div>
            <div className='pagination-item'>3</div>
            <div className='pagination-item'>...</div>
            <div className='pagination-item'>10</div>
            <ArrowIcon className='arrow-next arrow-active'/>
        </div>
        </>
    );
}

export default Pagination;