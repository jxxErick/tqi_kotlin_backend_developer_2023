<h1>jumarket-API</h1>
<p align="center">API Rest para um Sistema de Analise de Solicitação de Crédito</p>
<p align="center">
     <a alt="Java">
        <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white" />
    </a>
    <a alt="Kotlin">
        <img src="https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white" />
    </a>
    <a alt="Spring Boot">
        <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white" />
    </a>
    <a alt="Mysql ">
        <img src="https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white" />
    </a>
 </a>
    <a alt="Mysql ">
        <img src="https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white" />
    </a>
    <a alt="Flyway">
        <img src="https://img.shields.io/badge/Flyway-v9.5.1-red.svg">
    </a>
<a alt="Gradle">
        <img src="https://img.shields.io/badge/Gradle-v7.6-lightgreen.svg" />
    </a>

</p>

<h3 align="center" >Descrição do Projeto</h3 >


<p align="center"> Api de comercio online </p>

<h2> Usos </h2>

- Categorias

Pode-se manipular categorias da forma que quiser, editar, inserir uma passando o nome, editar, deletar, listar.

- Produtos

Já os produtos, além de todas informações que o enunciado pede, foi acrescentado o estoque de cada um deles, então pode ser passado também a quantidade que tem no estoque para inserir um, associando também a uma categoria. Já que cada categoria possui varios produtos. Coloquei metodos como, adicionar estoque ao produto, remover estoque do produto, editar produto, listar produto por categoria, remover produto e claro, o de criar.

- Usuario

Essa parte foi um acrescimo, onde é possivel ter um usuario, bem simples, apenas com cpf, email e nome. Cada usuario pode ter um carrinho de compra e várias vendas. Foi feito metodos para manipular tudo relacionado a ele.

- Carrinho

O carrinho recebe uma lista de Itens, e contém também um usuario, a quantidade de itens e o valor total. Nessa lista de itens foi criado uma classe a parte nomeada de "ItemCarrinho", onde é passado o preço unitario e a quantidade. Todos usuarios podem ter um unico carrinho, e todos carrinhos podem ter um unico usuario, então, assim que adicionado um produto ao carrinho, o usuario automaticamente gera esse carrinho, e, se o carrinho ficar vazio, ele é deletado. Os metodos possiveis para manipular são adicionar itens ao carrinho, remover, listar itens do carrinho de um usuario e deletar. 

- Venda

Para essa parte, podemos finalizar uma venda. Assim que finalizado a venda, o carrinho do usuario ficam vazio, é descontado o valor total do estoque e os itens vendidos vao para um histórico separado, para isso acontecer, é necessario passar a forma de pagamento e a id do usuario que deseja finalizar a venda. outro método é o de listar as vendas. 