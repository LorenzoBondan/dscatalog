import {ReactComponent as ArrowIcon} from 'assets/images/Seta.svg';
import ReactPaginate from 'react-paginate';
import './styles.css';

type Props = {
    pageCount : number;
    range : number;
    onChange?: (pageNumber : number ) => void;
    forcePage? : number;
}

const Pagination = ({pageCount, range, onChange, forcePage} : Props) => {
    return(
        <ReactPaginate 
            pageCount = {pageCount} // a qte de páginas total
            pageRangeDisplayed = {range} // quantas bolinhas da paginação irão aparecer no meio da paginação
            marginPagesDisplayed = {1} // a bolinha que aparece no final
            //estilização
            containerClassName= 'pagination-container'
            pageLinkClassName='pagination-item' // bolinhas
            breakClassName='pagination-item' // ...
            previousClassName='arrow-previous'
            nextClassName='arrow-next'
            previousLabel={<div className='pagination-arrow-container'><ArrowIcon/></div>} // anterior
            nextLabel={<div className='pagination-arrow-container'><ArrowIcon/></div>} // próximo
            activeLinkClassName='pagination-link-active'
            disabledClassName='arrow-inactive'
            //renderiza um ul 
            // evento troca de página
            onPageChange={(items) => onChange ? onChange(items.selected) : {}}
            forcePage={forcePage}
        />
    );
}

export default Pagination;