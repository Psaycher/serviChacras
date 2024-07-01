/* NavBar Funciones */

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
  
  document.getElementById("mensajes").addEventListener('click', function() {
    let notificationPopup = document.getElementById('noti-mensajes');
    
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
  
  function abrir_mensajes(){
      if(document.querySelector('.mensaje-container').style.display = 'none'){
          document.querySelector('.mensaje-container').style.display = 'block';
          document.getElementById('nombre-proveedor').textContent = document.getElementById('botonAbrirMensaje').textContent;
      } else{
        document.getElementById('nombre-proveedor').textContent = document.getElementById('botonAbrirMensaje').textContent;
      }
  }
  
  /*Fin navbar funciones*/
  
  /*Mensajeria*/
  document.getElementById("usuario-proveedor").addEventListener('click', function() {
      let detallesUsuario = document.getElementById('detalles-proveedor');
      let listItems = document.querySelectorAll('#detalles-proveedor .list-group-item');
      
      // Calcular la altura total de los elementos de la lista con un margen adicional
      let totalHeight = 0;
      listItems.forEach(item => {
          totalHeight += item.clientHeight;
      });
  
      // Agregar un margen adicional para asegurar que todos los elementos se muestren completamente
      totalHeight -= 95; // Ajustar según sea necesario
  
      if (detallesUsuario.style.height == '0px') {
          detallesUsuario.style.height = totalHeight + 'px';
          // Aquí debe ir la lógica para mostrar la notificación si corresponde
      } else {
          detallesUsuario.style.height = '0';
          // Aquí debe ir la lógica para ocultar la notificación si corresponde
      }
  });

   function detallesProveedorG() {
    let detallesUsuario = document.getElementById('detalles-proveedorG');
    let listItems = document.querySelectorAll('#detalles-proveedorG .list-group-item');
    
    // Calcular la altura total de los elementos de la lista con un margen adicional
    let totalHeight = 100;
    //listItems.forEach(item => {
    //    totalHeight += item.clientHeight;
    //});

    // Agregar un margen adicional para asegurar que todos los elementos se muestren completamente
    //totalHeight -= 95; // Ajustar según sea necesario

    if (detallesUsuario.style.height == '0px') {
        detallesUsuario.style.height = totalHeight + 'px';
        // Aquí debe ir la lógica para mostrar la notificación si corresponde
    } else {
        detallesUsuario.style.height = '0';
        // Aquí debe ir la lógica para ocultar la notificación si corresponde
    }
   }
  
  function minimizar(){
      if(document.getElementById('minimizar').textContent=="👇"){
        document.getElementById('minimizar').textContent="👆";
      } else{
        document.getElementById('minimizar').textContent="👇";
      }
      let content = document.getElementById('mensaje-content');
      let form = document.getElementById('mensaje-form');
      let preview = document.getElementById('preview-container')
      content.style.display = content.style.display === 'none' ? 'block' : 'none';
      form.style.display = form.style.display === 'none' ? 'flex' : 'none';
      preview.style.display = form.style.display === 'none' ? 'flex' : 'none';
  }
  
  function cerrarMensaje(){
      document.querySelector('.mensaje-container').style.display = 'none';
  }
  
  function enviarMensaje() {
      let input = document.querySelector('.mensaje-form input[type="text"]');
      let mensaje = input.value.trim();
      let fileInput = document.getElementById('file-upload');
      let files = fileInput.files;
      let mensajeContent = document.querySelector('.mensaje-content');
  
      if (files.length > 0) {
          Array.from(files).forEach(file => {
              const nuevoMensaje = document.createElement('div');
              nuevoMensaje.classList.add('mensaje', 'usuario');
              const reader = new FileReader();
              reader.onload = function(e) {
                  nuevoMensaje.innerHTML = `<div class="texto"><img src="${e.target.result}" class="message-image" onclick="viewImage('${e.target.result}')"></div>`;
                  mensajeContent.appendChild(nuevoMensaje);
                  mensajeContent.scrollTop = mensajeContent.scrollHeight;
              }
              reader.readAsDataURL(file);
          });
      }
  
      if (mensaje) {
          const nuevoMensaje = document.createElement('div');
          nuevoMensaje.classList.add('mensaje', 'usuario');
          nuevoMensaje.innerHTML = `<div class="texto">${mensaje}</div>`;
          mensajeContent.appendChild(nuevoMensaje);
          input.value = '';
          mensajeContent.scrollTop = mensajeContent.scrollHeight;
      }
  
      fileInput.value = '';
      document.getElementById('preview-container').innerHTML = '';
  }
  
  // Función para manejar el cambio de archivos adjuntos
  function handleFileChange(event) {
      const files = event.target.files;
      const previewContainer = document.getElementById('preview-container');
      previewContainer.innerHTML = '';
  
      Array.from(files).forEach((file, index) => {
          if (file.type.startsWith('image/')) {
              const reader = new FileReader();
              reader.onload = function(e) {
                  const previewImage = document.createElement('div');
                  previewImage.classList.add('preview-image');
                  previewImage.innerHTML = `
                      <img src="${e.target.result}" alt="Imagen adjunta" onclick="viewImage('${e.target.result}')">
                      <button class="close-preview" data-index="${index}" onclick="removePreview(${index})">&times;</button>
                  `;
                  previewContainer.appendChild(previewImage);
              }
              reader.readAsDataURL(file);
          } else {
              console.error('File type not supported:', file.type);
          }
      });
  
      previewContainer.style.display = files.length > 0 ? 'flex' : 'none';
  }
  
  function removePreview(index) {
      const fileInput = document.getElementById('file-upload');
      const dt = new DataTransfer();
      const files = Array.from(fileInput.files);
  
      files.splice(index, 1);
      files.forEach(file => dt.items.add(file));
      fileInput.files = dt.files;
  
      const previewContainer = document.getElementById('preview-container');
      const previewImages = previewContainer.getElementsByClassName('preview-image');
  
      if (previewImages[index]) {
          previewImages[index].remove();
      }
  
      // Adjust the data-index of remaining preview images
      for (let i = 0; i < previewImages.length; i++) {
          previewImages[i].querySelector('.close-preview').setAttribute('data-index', i);
      }
  
      previewContainer.style.display = files.length > 0 ? 'flex' : 'none';
  }
  
  function handlePreviewContainerClick(event) {
      if (event.target.classList.contains('close-preview')) {
          const index = event.target.getAttribute('data-index');
          removePreview(index);
      }
  }
  
  function handleImageViewerClick(event) {
      if (event.target.classList.contains('close-viewer')) {
          document.getElementById('image-viewer').style.display = 'none';
      }
  }
  
  function viewImage(src) {
      const viewer = document.getElementById('image-viewer');
      const viewerImage = document.getElementById('viewer-image');
      const downloadLink = document.getElementById('download-link');
  
      viewerImage.src = src;
      downloadLink.href = src;
      viewer.style.display = 'flex';
  }
  
  //Buscador de la mensajeria grande
  document.getElementById('searchUsuarios').addEventListener('input', function() {
      let filter = this.value.toLowerCase();
      let buttons = document.querySelectorAll('#chatsUsuarios .btn');
  
      buttons.forEach(button => {
          let name = button.getAttribute('name').toLowerCase();
          if (name.includes(filter)) {
              button.style.display = '';
          } else {
              button.style.display = 'none';
          }
      });
  });
  /*Fin de Mensajeria*/
  
  /*Boton calificar*/
  // Mostrar el formulario de calificación
  function mostrarFormularioCalificacion() {
      document.getElementById('overlayCalificacion').style.display = 'block';
      document.getElementById('formularioCalificacion').style.display = 'block';
  }
  
  // Cerrar el formulario de calificación
  function cerrarFormularioCalificacion() {
      document.getElementById('overlayCalificacion').style.display = 'none';
      document.getElementById('formularioCalificacion').style.display = 'none';
  }
  
  function calificar() {
      let botones = document.querySelectorAll('.calificarBtn');
      botones.forEach(boton => {
          boton.textContent = "Modificar Calificación";
      });
      mostrarFormularioCalificacion();
  }
  
  // Validar el formulario antes de enviarlo
  function validarFormulario(event) {
      const puntuacion = parseFloat(document.getElementById('puntuacion').value);
  
      if (isNaN(puntuacion) || puntuacion < 0 || puntuacion > 5) {
          event.preventDefault();
          document.getElementById('errorPuntuacion').style.display = 'block';
      } else {
          document.getElementById('errorPuntuacion').style.display = 'none';
          // Si los datos son válidos, permite el envío del formulario
          console.log('Formulario válido, enviar datos...');
      }
  }
  /*Fin de boton calificar*/
  
  /*Boton Pagar*/
  // Función para abrir el modal
  function showModal() {
      document.getElementById('cerrarModal').style.display = 'block';
      document.getElementById('modal').style.display = 'block';
  }
  
  // Función para cerrar el modal
  function hideModal() {
      document.getElementById('cerrarModal').style.display = 'none';
      document.getElementById('modal').style.display = 'none';
  }
  /*Fin de boton pagar*/
  
  /*Boton Reportar*/
  // Función para reportar
  function reportar() {
      document.getElementById('reportFormOverlay').style.display = 'block';
      document.getElementById('reportForm').style.display = 'block';
  }
  
  function reportClose() {
      document.getElementById('reportFormOverlay').style.display = 'none';
      document.getElementById('reportForm').style.display = 'none';
  }
  
  function formularioReporte(event) {
      event.preventDefault();
      // Aquí puedes añadir la lógica para enviar el formulario
      // Luego de enviar el formulario, puedes cerrar el modal:
      document.getElementById('reportFormOverlay').style.display = 'none';
      document.getElementById('reportForm').style.display = 'none';
  }
  /*Fin de boton reportar*/
  
  /*Boton Solicitar Servicio*/
  // Función para abrir el formulario de servicio
  function openForm() {
      document.getElementById("overlay").style.display = "block";
      document.getElementById("serviceForm").style.display = "block";
  }
  
  // Función para cerrar el formulario de servicio
  function closeForm() {
      document.getElementById("overlay").style.display = "none";
      document.getElementById("serviceForm").style.display = "none";
  }
  /*Fin de boton solicitar servicio*/
