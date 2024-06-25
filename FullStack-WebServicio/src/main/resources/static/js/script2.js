document.getElementById("usuario").addEventListener('click', function() {
    let detallesUsuario = document.getElementById('detalles-usuario');
    let listItems = document.querySelectorAll('#detalles-usuario .list-group-item');
    
    // Calcular la altura total de los elementos de la lista con un margen adicional
    let totalHeight = 0;
    listItems.forEach(item => {
        totalHeight += item.clientHeight;
    });

    // Agregar un margen adicional para asegurar que todos los elementos se muestren completamente
    totalHeight += 8; // Puedes ajustar este valor según sea necesario

    if (detallesUsuario.style.height == '0px') {
        detallesUsuario.style.height = totalHeight + 'px';
        notificationPopup.classList.add('show');
    } else {
        detallesUsuario.style.height = '0';
        notificationPopup.classList.remove('show');
    }
});

document.getElementById("campana").addEventListener('click', function() {
    let notificationPopup = document.getElementById('notification');
    let listItems = document.querySelectorAll('#notification .list-group-item');
    
    // Calcular la altura total de los elementos de la lista con un margen adicional
    let totalHeight = 0;
    listItems.forEach(item => {
        totalHeight += item.clientHeight;
    });

    // Agregar un margen adicional para asegurar que todos los elementos se muestren completamente
    totalHeight += 12; // Puedes ajustar este valor según sea necesario

    if (notificationPopup.style.height == '0px') {
        notificationPopup.style.height = totalHeight + 'px';
        notificationPopup.classList.add('show');
        document.querySelector('#campana .notification-bell .notification-count').textContent="0";
    } else {
        notificationPopup.style.height = '0';
        notificationPopup.classList.remove('show');
    }
});

  let primeraVez=true;

  document.getElementById("mensajes").addEventListener('click', function() {
    let notificationPopup = document.getElementById('noti-mensajes');
    let listGroup = document.querySelector('#noti-mensajes .list-group');

    // Crear los mensajes
    if (primeraVez) {
      let mensajes = ["Cliente N°1", "Cliente N°2", "Cliente N°3"];
      mensajes.forEach((cliente, index) => {
          let li = document.createElement("li");
          li.classList.add('list-group-item');
          let boton = document.createElement("button");
          boton.textContent = cliente;
          boton.classList.add('btn', 'btn-outline-light');
          boton.setAttribute('type', 'button');
          boton.addEventListener("click", function() {
            if(document.querySelector('.mensaje-container').style.display = 'none'){
              document.querySelector('.mensaje-container').style.display = 'block';
              document.getElementById('nombre-proveedor').textContent = boton.textContent;
            } else{
              document.getElementById('nombre-proveedor').textContent = boton.textContent;
            }
          });
          li.append(boton);
          listGroup.append(li);
      });
      primeraVez = false;
    }
    
    let listItems = document.querySelectorAll('#noti-mensajes .list-group-item');

    // Calcular la altura total de los elementos de la lista con un margen adicional
    let totalHeight = 0;
    listItems.forEach(item => {
        totalHeight += item.clientHeight;
    });

    // Agregar un margen adicional para asegurar que todos los elementos se muestren completamente
    totalHeight += 8; // Puedes ajustar este valor según sea necesario

    if (notificationPopup.style.height == '0px' || notificationPopup.style.height === '') {
        notificationPopup.style.height = totalHeight + 'px';
        notificationPopup.classList.add('show');
    } else {
        notificationPopup.style.height = '0';
        notificationPopup.classList.remove('show');
    }
  });

  document.getElementById("filtro").addEventListener('click', function() {
    let detallesFiltro = document.getElementById('detalles-filtro');
    let listItems = document.querySelectorAll('#detalles-filtro .list-group-item');
    
    // Calcular la altura total de los elementos de la lista con un margen adicional
    let totalHeight = 0;
    listItems.forEach(item => {
        totalHeight += item.clientHeight;
    });

    // Agregar un margen adicional para asegurar que todos los elementos se muestren completamente
    totalHeight += 8; // Puedes ajustar este valor según sea necesario

    if (detallesFiltro.style.height == '0px') {
        detallesFiltro.style.height = totalHeight + 'px';
        notificationPopup.classList.add('show');
    } else {
        detallesFiltro.style.height = '0';
        notificationPopup.classList.remove('show');
    }
  });


  // Evento para validar el formulario al enviarlo
  calificacionFormulario.addEventListener('submit', validarFormulario);

  let cambiarFuncion= document.getElementById("cambiar-funcion");

  cambiarFuncion.addEventListener('click', () => {
    if(cambiarFuncion.textContent === "Notificar trabajo completo"){
        cambiarFuncion.textContent = "Pedir pago";
    }else if(cambiarFuncion.textContent === "Pedir pago"){
        cambiarFuncion.textContent = "Pago recibido";
        cambiarFuncion.classList.remove('btn-outline-warning');
        cambiarFuncion.classList.add('btn-outline-success');
    } else if(cambiarFuncion.textContent === "Pago recibido"){
        cambiarFuncion.textContent = "Calificar";
    } else if(cambiarFuncion.textContent === "Calificar"){
        mostrarFormularioCalificacion();
        cambiarFuncion.textContent = "Modificar Calificacion";
    } else{
        mostrarFormularioCalificacion();
        cambiarFuncion.textContent = "Notificar trabajo completo";
        cambiarFuncion.classList.remove('btn-outline-success');
        cambiarFuncion.classList.add('btn-outline-warning');
    }
  })


  // ! Filtro

  document.getElementById("filtro").addEventListener('click', function () {
    let detallesFiltro = document.getElementById('detalles-filtro');
    let listItems = document.querySelectorAll('#detalles-filtro .list-group-item');
    let botonFiltro = document.getElementById('filtro');

    // Calcular la altura total de los elementos de la lista con un margen adicional
    let totalHeight = 0;
    listItems.forEach(item => {
        totalHeight += item.clientHeight;
    });

    // Agregar un margen adicional para asegurar que todos los elementos se muestren completamente
    totalHeight += 58; // Puedes ajustar este valor según sea necesario

    // Calcular la posición del botón y ajustar la posición del formulario
    let rect = botonFiltro.getBoundingClientRect();
    detallesFiltro.style.top = rect.bottom + 'px';
    detallesFiltro.style.left = rect.left + 'px';

    if (detallesFiltro.style.height == '0px' || detallesFiltro.style.height === '') {
        detallesFiltro.style.height = totalHeight + 'px';
        detallesFiltro.classList.add('show');
    } else {
        detallesFiltro.style.height = '0';
        detallesFiltro.classList.remove('show');
    }
});


document.addEventListener('DOMContentLoaded', function () {
    // Obtener los elementos del filtro y del contenedor de tarjetas
    const filtroForm = document.getElementById('Formulario-filtro');
    const cardContainer = document.getElementById('card-container');

    // Datos simulados (deberías reemplazarlos con los datos reales)
    const data = [
        {
            categoria: 'Ruta2km69',
            title: 'Servicio 1',
            imgSrc: 'ruta/a/la/imagen1.jpg',
            description: 'Descripción del servicio 1',
            items: ['Item 1', 'Item 2', 'Item 3'],
            links: ['#', '#']
        },
        {
            categoria: 'Ruta2km75',
            title: 'Servicio 2',
            imgSrc: 'ruta/a/la/imagen2.jpg',
            description: 'Descripción del servicio 2',
            items: ['Item 1', 'Item 2', 'Item 3'],
            links: ['#', '#']
        },
        // Más datos aquí...
    ];

    // Función para generar las tarjetas
    function generateCards(categoria) {
        // Limpiar el contenedor de tarjetas
        cardContainer.innerHTML = '';

        // Filtrar los datos según la categoría seleccionada
        const filteredData = data.filter(item => item.categoria === categoria);

        // Generar las tarjetas
        filteredData.forEach(item => {
            const card = document.createElement('div');
            card.className = 'card';
            card.style.width = '18rem';

            const img = document.createElement('img');
            img.src = item.imgSrc;
            img.className = 'card-img-top';
            img.alt = item.title;

            const cardBody = document.createElement('div');
            cardBody.className = 'card-body';

            const cardTitle = document.createElement('h5');
            cardTitle.className = 'card-title';
            cardTitle.textContent = item.title;

            const cardText = document.createElement('p');
            cardText.className = 'card-text';
            cardText.textContent = item.description;

            cardBody.appendChild(cardTitle);
            cardBody.appendChild(cardText);

            const listGroup = document.createElement('ul');
            listGroup.className = 'list-group list-group-flush';

            item.items.forEach(listItem => {
                const li = document.createElement('li');
                li.className = 'list-group-item';
                li.textContent = listItem;
                listGroup.appendChild(li);
            });

            const cardBodyLinks = document.createElement('div');
            cardBodyLinks.className = 'card-body';

            item.links.forEach(link => {
                const a = document.createElement('a');
                a.href = link;
                a.className = 'card-link';
                a.textContent = 'Card link';
                cardBodyLinks.appendChild(a);
            });

            card.appendChild(img);
            card.appendChild(cardBody);
            card.appendChild(listGroup);
            card.appendChild(cardBodyLinks);

            cardContainer.appendChild(card);
        });
    }

    // Escuchar el evento submit del formulario del filtro
    filtroForm.addEventListener('submit', function (event) {
        event.preventDefault();
        const formData = new FormData(filtroForm);
        const categoriaSeleccionada = formData.get('barrio'); // Ajusta el nombre según el select de tu filtro

        generateCards(categoriaSeleccionada);
    });
});
