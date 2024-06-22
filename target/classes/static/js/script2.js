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