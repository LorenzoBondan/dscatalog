import {ReactComponent as SearchIcon} from 'assets/images/search-icon.svg';
import { useEffect, useState } from 'react';
import { Controller, useForm } from 'react-hook-form';
import Select from 'react-select';
import { Category } from 'types/category';
import { requestBackend } from 'util/requests';
import './styles.css'

export type ProductFilterData = {
    name : string;
    category : Category | null;
}

type Props = {
    onSubmitFilter : (data: ProductFilterData) => void;
}

const ProductFilter = ( {onSubmitFilter} : Props) => {

    const { register, handleSubmit, control, setValue, getValues } = useForm<ProductFilterData>();

    const [selectCategories, setSelectCategories] = useState<Category[]>();
    
    //trazer as categorias pra povoar o combobox
    useEffect(() => {
        requestBackend({url: '/categories'})
            .then(response => {
                setSelectCategories(response.data.content)
            })
    }, []);

    //enviar o form (fazer a busca filtrada)
    const onSubmit = (formData : ProductFilterData) => {
        onSubmitFilter(formData);
    };

    // limpar
    const handleFormClear = () => {
        setValue('name', '');
        setValue('category', null);
    }

    // enviar form cada vez que a categoria mudar (fazer a busca filtrada por categoria)
    const handleChangeCategory = (value: Category) => {
        setValue('category', value);

        const obj : ProductFilterData = {
            name: getValues('name'), 
            category: getValues('category'), 
        };

        onSubmitFilter(obj);
    }

    return(
        <div className="base-card product-filter-container">
            <form onSubmit={handleSubmit(onSubmit)} className='product-filter-form'>
                <div className='product-filter-name-container'>
                <input 
                    {...register("name")}
                    type="text"
                    className={`form-control`}
                    placeholder="Nome do produto"
                    name="name"
                />
                <button className='product-filter-button-search-icon'>
                    <SearchIcon/>
                </button>
                </div>
                <div className='product-filter-bottom-container'>
                    <div className='product-filter-category-container'>
                        <Controller 
                            name = 'category'
                            control = {control}
                            render = {( {field} ) => (
                            <Select 
                                {...field}
                                options={selectCategories}
                                isClearable
                                classNamePrefix="product-filter-select"
                                placeholder="Categoria"
                                getOptionLabel={(category: Category) => category.name}
                                getOptionValue={(category: Category) => category.id.toString()}

                                onChange={value => handleChangeCategory(value as Category)}
                            />    
                            )}
                        />
                    </div>
                    <button onClick={handleFormClear} className='btn btn-outline-secondary btn-product-filter-clear'>
                        LIMPAR <span className='btn-product-filter-word'>FILTRO</span>
                    </button>
                </div>
            </form>
        </div>
    );
}

export default ProductFilter;