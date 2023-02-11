import { AxiosRequestConfig } from 'axios';
import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { useHistory, useParams } from 'react-router-dom';
import Select from 'react-select';
import { Product } from 'types/product';
import { requestBackend } from 'util/requests';
import './styles.css';

type UrlParams = {
    productId: string;
}

const Form = () => {

    const options = [
        { value: 'chocolate', label: 'Chocolate' },
        { value: 'strawberry', label: 'Strawberry' },
        { value: 'vanilla', label: 'Vanilla' }
      ]

    const { productId } = useParams<UrlParams>();
    
    const isEditing = productId !== 'create';

    const { register, handleSubmit, formState: {errors}, setValue } = useForm<Product>();

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

    const onSubmit = (formData : Product) => {

        const data = {...formData,
            imgUrl: isEditing? formData.imgUrl : "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/24-big.jpg",
            categories: isEditing? formData.categories : [ {id:1, name: ""} ]
        }

        //data : formData
        const params : AxiosRequestConfig = {
            method: isEditing? "PUT" : "POST",
            url: isEditing? `/products/${productId}` : "/products",
            data: data,
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
                                
                                <Select 
                                    options={options}
                                    classNamePrefix="product-crud-select"
                                    placeholder="Categoria"
                                    isMulti
                                />

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