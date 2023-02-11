import { AxiosRequestConfig } from 'axios';
import { useEffect, useState } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { useHistory, useParams } from 'react-router-dom';
import Select from 'react-select';
import { Category } from 'types/category';
import { Product } from 'types/product';
import { requestBackend } from 'util/requests';
import './styles.css';

type UrlParams = {
    productId: string;
}

const Form = () => {

    const { productId } = useParams<UrlParams>();
    
    const isEditing = productId !== 'create';

    const { register, handleSubmit, formState: {errors}, setValue, control } = useForm<Product>();

    //trazer as categorias pra povoar o combobox
    useEffect(() => {
        requestBackend({url: '/categories'})
            .then(response => {
                setSelectCategories(response.data.content)
            })
    }, []);

    //carregar as textboxes com os valores do produto a ser editado
    useEffect(() => {
        if (isEditing) {
            requestBackend({url:`/products/${productId}`})
                .then((response) => {

                    const product = response.data as Product;

                    setValue('name', product.name);
                    setValue('price', product.price);
                    setValue('description', product.description);
                    setValue('imgUrl', product.imgUrl);
                    setValue('categories', product.categories);
                })
        }
    }, [isEditing, productId, setValue]);

    const history = useHistory();

    const [selectCategories, setSelectCategories] = useState<Category[]>();

    const onSubmit = (formData : Product) => {

        const params : AxiosRequestConfig = {
            method: isEditing? "PUT" : "POST",
            url: isEditing? `/products/${productId}` : "/products",
            data: formData,
            withCredentials: true
          };

        requestBackend(params)
        .then(response => {
            console.log('SUCESSO', response.data);
            history.push("/admin/products");
        });
    };

    // botão de cancelar -> reenvia o usuário para a lista de produtos, saindo do form
    const handleCancel = () => {
        history.push("/admin/products")
    }
    
    return(
        <div className="product-crud-container">

            <div className="base-card product-card-form-card">
                <h1>CADASTRAR UM PRODUTO</h1>

                <form onSubmit={handleSubmit(onSubmit)}>
                    <div className='row product-crud-inputs-container'>
                        <div className='col-lg-6 product-crud-inputs-left-container'>

                            <div className='margin-bottom-30'>
                                
                                <input 
                                    {...register("name", {
                                    required: 'Campo obrigatório',
                                    })}
                                    type="text"
                                    className={`form-control base-input ${errors.name ? 'is-invalid' : ''}`}
                                    placeholder="Nome do Produto"
                                    name="name"
                                />
                                <div className='invalid-feedback d-block'>{errors.name?.message}</div>

                            </div>




                            <div className='margin-bottom-30'>

                                <Controller 
                                    name = 'categories'
                                    rules = {{required: true}}
                                    control = {control}
                                    render = {( {field} ) => (
                                        <Select 
                                            {...field}
                                            options={selectCategories}
                                            classNamePrefix="product-crud-select"
                                            placeholder="Categoria"
                                            isMulti
                                            getOptionLabel={(category: Category) => category.name}
                                            getOptionValue={(category: Category) => category.id.toString()}
                                        />    
                                    )}
                                />
                                {errors.categories && (
                                    <div className='invalid-feedback d-block'>Campo obrigatório</div>
                                )}
                                
                            </div>




                            <div className='margin-bottom-30'>
                                
                                <input 
                                    {...register("price", {
                                    required: 'Campo obrigatório',
                                    })}
                                    type="number"
                                    className={`form-control base-input ${errors.price ? 'is-invalid' : ''}`}
                                    placeholder="Preço"
                                    name="price"
                                />
                                <div className='invalid-feedback d-block'>{errors.name?.message}</div>

                            </div>


                            <div className='margin-bottom-30'>
                                
                                <input 
                                    {...register("imgUrl", {
                                    required: 'Campo obrigatório',
                                    pattern: { 
                                        value: /^(https?|chrome):\/\/[^\s$.?#].[^\s]*$/gm,
                                        message: 'Insira uma URL válida'
                                    }
                                    })}
                                    type="text"
                                    className={`form-control base-input ${errors.imgUrl ? 'is-invalid' : ''}`}
                                    placeholder="URL da imagem do Produto"
                                    name="imgUrl"
                                />
                                <div className='invalid-feedback d-block'>{errors.imgUrl?.message}</div>

                            </div>

                        </div>

                        <div className='col-lg-6'>
                            <div>
                                <textarea 
                                    rows={10} 
                                    {...register("description", {
                                    required: 'Campo obrigatório',
                                    })}
                                    className={`form-control base-input ${errors.description ? 'is-invalid' : ''} h-auto`}
                                    placeholder="Descrição"
                                    name="description"
                                />
                                <div className='invalid-feedback d-block'>{errors.description?.message}</div>
                            </div>
                        </div>
                    </div>

                    <div className='product-crud-buttons-container'>
                        <button 
                            className='btn btn-outline-danger product-crud-buttons'
                            onClick={handleCancel}
                            >
                            CANCELAR
                        </button>

                        <button className='btn btn-primary text-white product-crud-buttons'>SALVAR</button>

                    </div>
                </form>
            </div>
            
        </div>
    );
}

export default Form;