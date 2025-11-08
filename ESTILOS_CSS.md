# Documentación de Estilos CSS del Proyecto

## 📋 Resumen

Este proyecto ahora cuenta con un sistema de estilos CSS consistente y moderno que garantiza una experiencia visual uniforme en todas las vistas.

## 🎨 Archivos CSS Disponibles

### 1. **style.css** - Estilos Base Globales
**Ubicación:** `/src/main/resources/assets/style.css`  
**Uso:** Vistas de login, registro, eliminación, confirmaciones, etc.

**Características:**
- ✅ Diseño centrado con gradiente morado/azul de fondo
- ✅ Contenedores con sombras y bordes redondeados
- ✅ Formularios con campos estilizados y focus states
- ✅ Botones con efectos hover y gradientes
- ✅ Alertas y mensajes flash (success, error, warning, info)
- ✅ Tablas responsivas
- ✅ Sistema de espaciado con clases utilitarias

**Clases principales:**
```css
.container          /* Contenedor principal centrado */
.btn                /* Botón primario con gradiente */
.btn-success        /* Botón verde para acciones positivas */
.btn-danger         /* Botón rojo para acciones destructivas */
.btn-secondary      /* Botón gris para acciones secundarias */
.alert              /* Base para alertas */
.alert-success      /* Alerta verde de éxito */
.alert-error        /* Alerta roja de error */
.alert-warning      /* Alerta amarilla de advertencia */
.alert-info         /* Alerta azul informativa */
.hecho-card         /* Tarjeta para mostrar hechos */
.mt-1, .mt-2, .mt-3 /* Margin top (10px, 20px, 30px) */
.mb-1, .mb-2, .mb-3 /* Margin bottom (10px, 20px, 30px) */
.text-center        /* Texto centrado */
```

---

### 2. **main.css** - Estilos para Búsqueda y Home
**Ubicación:** `/src/main/resources/assets/main.css`  
**Uso:** Vista de home, búsqueda de hechos

**Características:**
- ✅ Fondo claro (#f5f7fa)
- ✅ Formularios de búsqueda con grid responsivo
- ✅ Tarjetas de resultados con hover effects
- ✅ Botones de búsqueda y limpieza
- ✅ Sección de usuario con badges

**Clases principales:**
```css
.container          /* Contenedor principal con fondo blanco */
.page-title         /* Título principal de la página */
.page-message       /* Mensaje descriptivo */
.user-section       /* Sección de información del usuario */
.user-registered    /* Badge de usuario registrado (verde) */
.user-anonymous     /* Badge de usuario anónimo (gris) */
.search-form        /* Formulario de búsqueda */
.form-grid          /* Grid responsivo para campos */
.form-group         /* Grupo de formulario (label + input) */
.btn-search         /* Botón de búsqueda (verde) */
.btn-clear          /* Botón de limpiar (gris) */
.hecho-card         /* Tarjeta para mostrar hechos */
.result-item        /* Item de resultado de búsqueda */
.result-meta        /* Metadatos del resultado */
.btn-delete         /* Botón de eliminar (rojo) */
```

---

### 3. **creacion.css** - Estilos para Formularios de Creación
**Ubicación:** `/src/main/resources/assets/creacion.css`  
**Uso:** Vistas de creación de hechos y colecciones

**Características:**
- ✅ Formulario centrado con gradiente de fondo
- ✅ Preview de archivos multimedia
- ✅ Inputs y textareas con estilos consistentes
- ✅ Mensajes flash integrados
- ✅ Totalmente responsivo

**Clases principales:**
```css
.form-container     /* Contenedor del formulario */
.flash              /* Mensaje flash */
.flash.error        /* Mensaje de error */
.flash.success      /* Mensaje de éxito */
.preview            /* Contenedor de preview multimedia */
#preview-content    /* Contenido del preview (img/video) */
```

---

### 4. **busqueda-hechos.css** - Estilos para Búsqueda de Hechos con Vista Dual
**Ubicación:** `/src/main/resources/assets/busqueda-hechos.css`  
**Uso:** Vista de búsqueda de hechos con lista y mapa

**Características:**
- ✅ Vista dual: Lista de tarjetas y Mapa interactivo (Leaflet)
- ✅ Formulario de búsqueda con grid responsivo
- ✅ Controles de vista con botones de alternancia
- ✅ Tarjetas de hechos con hover effects y badges
- ✅ Mapa integrado con popups personalizados
- ✅ Animaciones de transición entre vistas
- ✅ Totalmente responsivo

**Clases principales:**
```css
.container              /* Contenedor principal */
.page-header            /* Encabezado con gradiente */
.search-section         /* Sección del formulario */
.search-form            /* Formulario de búsqueda */
.form-grid              /* Grid para campos de búsqueda */
.form-group             /* Grupo de formulario */
.form-actions           /* Contenedor de botones */
.btn-primary            /* Botón de búsqueda */
.btn-secondary          /* Botón de limpiar */
.results-section        /* Sección de resultados */
.results-header         /* Encabezado de resultados */
.badge                  /* Badge de conteo */
.view-controls          /* Controles de alternancia */
.btn-view               /* Botón de vista (lista/mapa) */
.btn-view.active        /* Vista activa */
.view-container         /* Contenedor de vista */
.view-container.active  /* Vista visible */
.hechos-grid            /* Grid de tarjetas */
.hecho-card             /* Tarjeta de hecho */
.hecho-header           /* Encabezado de tarjeta */
.categoria-badge        /* Badge de categoría */
.hecho-descripcion      /* Descripción del hecho */
.hecho-details          /* Detalles del hecho */
.detail-item            /* Item de detalle */
.hecho-footer           /* Footer de tarjeta */
.btn-small              /* Botón pequeño */
#map                    /* Contenedor del mapa */
.mapa-leyenda           /* Leyenda del mapa */
.popup-content          /* Contenido de popup */
.no-results             /* Mensaje sin resultados */
```

**Integración con Leaflet:**
- Carga automática de mapa al cambiar a vista de mapa
- Marcadores con popups informativos
- Centrado automático basado en resultados
- Estilos personalizados para popups

---

### 5. **mapa-hechos.css** - Estilos para Vista de Mapa Dedicada
**Ubicación:** `/src/main/resources/assets/mapa-hechos.css`  
**Uso:** Vista de mapa completa (uso independiente)

**Características:**
- ✅ Mapa de pantalla completa optimizado
- ✅ Controles y estadísticas del mapa
- ✅ Leyenda y tarjetas de información
- ✅ Popups estilizados para Leaflet
- ✅ Grid de estadísticas con gradientes
- ✅ Responsivo para móviles

**Clases principales:**
```css
.mapa-container         /* Contenedor del mapa */
#map                    /* Mapa Leaflet */
.mapa-controles         /* Controles superiores */
.mapa-info              /* Información del mapa */
.btn                    /* Botones de acción */
.mapa-leyenda           /* Leyenda del mapa */
.mapa-estadisticas      /* Grid de estadísticas */
.stat-card              /* Tarjeta de estadística */
.stat-value             /* Valor numérico */
.popup-content          /* Contenido de popup */
.categoria-badge        /* Badge de categoría */
```

---

### 6. **dashboard.css** - Estilos para Panel Administrativo
**Ubicación:** `/src/main/resources/assets/dashboard.css`  
**Uso:** Todas las vistas del dashboard administrativo

**Características:**
- ✅ Layout con sidebar fijo y contenido principal
- ✅ Sidebar con gradiente y navegación
- ✅ Widgets y tarjetas de estadísticas
- ✅ Tablas con hover effects
- ✅ Sistema completo de botones
- ✅ Badges y etiquetas
- ✅ Responsive (móvil friendly)

**Clases principales:**
```css
body.dashboard-body     /* Body del dashboard */
.dashboard-container    /* Contenedor flex principal */
.sidebar                /* Menú lateral fijo */
.sidebar-header         /* Encabezado del sidebar */
.sidebar-nav            /* Navegación del sidebar */
.sidebar-footer         /* Footer del sidebar */
.main-content          /* Contenido principal */
.main-header           /* Encabezado del contenido */
.widgets-grid          /* Grid de widgets */
.widget                /* Widget individual */
.widget-dato           /* Dato numérico del widget */
.section               /* Sección de contenido */
.table-container       /* Contenedor de tabla */
.form-group            /* Grupo de formulario */
.btn                   /* Botón base */
.btn-primary           /* Botón primario (morado) */
.btn-success           /* Botón de éxito (verde) */
.btn-danger            /* Botón de peligro (rojo) */
.btn-warning           /* Botón de advertencia (amarillo) */
.btn-secondary         /* Botón secundario (gris) */
.btn-sm, .btn-lg       /* Tamaños de botón */
.alert-success         /* Alerta de éxito */
.alert-error           /* Alerta de error */
.alert-warning         /* Alerta de advertencia */
.alert-info            /* Alerta informativa */
.stats-container       /* Contenedor de estadísticas */
.stat-card             /* Tarjeta de estadística */
.list-group            /* Lista grupal */
.list-group-item       /* Item de lista */
.badge                 /* Badge/etiqueta */
.badge-primary         /* Badge primario */
.badge-success         /* Badge de éxito */
.badge-danger          /* Badge de peligro */
.badge-warning         /* Badge de advertencia */
.badge-info            /* Badge informativo */
.d-flex                /* Display flex */
.justify-between       /* Justify content space-between */
.align-center          /* Align items center */
.gap-1, .gap-2, .gap-3 /* Gaps (10px, 20px, 30px) */
```

---

## 🎯 Paleta de Colores Consistente

### Colores Primarios
- **Primario:** `#667eea` → `#764ba2` (Gradiente morado/azul)
- **Éxito:** `#48bb78` → `#38a169` (Gradiente verde)
- **Peligro:** `#f56565` → `#c53030` (Gradiente rojo)
- **Advertencia:** `#ecc94b` → `#d69e2e` (Gradiente amarillo)
- **Info:** `#4299e1` (Azul claro)
- **Secundario:** `#6c757d` (Gris)

### Colores de Fondo
- **Fondo principal:** `#f5f7fa` (Gris muy claro)
- **Blanco:** `#ffffff`
- **Fondo alternativo:** `#f8f9fa`

### Colores de Texto
- **Texto principal:** `#333`
- **Texto secundario:** `#666`
- **Texto claro:** `#888`
- **Texto oscuro:** `#555`

### Colores de Bordes
- **Borde principal:** `#e0e0e0`
- **Borde claro:** `#f0f0f0`

---

## 📱 Responsive Design

Todos los archivos CSS incluyen breakpoints responsivos:

- **Desktop:** > 1024px (diseño completo)
- **Tablet:** 768px - 1024px (ajustes medios)
- **Mobile:** < 768px (layout vertical, sidebar completo)
- **Small Mobile:** < 480px (optimización para pantallas pequeñas)

---

## ✨ Efectos y Animaciones

### Animaciones incluidas:
- ✅ `fadeIn` - Aparición suave de contenedores
- ✅ `slideIn` - Deslizamiento de alertas
- ✅ Hover effects en botones (translateY + box-shadow)
- ✅ Transitions suaves en enlaces y formularios
- ✅ Focus states con sombras de color

---

## 🔧 Uso Recomendado por Vista

| Vista | CSS a usar | Clase body |
|-------|-----------|-----------|
| Login/Registro | `style.css` | (ninguna) |
| Home | `main.css` | (ninguna) |
| **Búsqueda de Hechos (Lista/Mapa)** | **`busqueda-hechos.css`** | **(ninguna)** |
| **Mapa de Hechos Dedicado** | **`mapa-hechos.css`** | **(ninguna)** |
| Crear Hecho/Colección | `creacion.css` | (ninguna) |
| Dashboard Admin | `dashboard.css` | `dashboard-body` |
| Estadísticas | `dashboard.css` | `dashboard-body` |
| Gestión de Solicitudes | `dashboard.css` | `dashboard-body` |
| Eliminación | `style.css` | (ninguna) |
| Confirmaciones | `style.css` | (ninguna) |

---

## 📝 Ejemplos de Uso

### Botón de Acción Principal
```html
<button type="submit" class="btn">Enviar</button>
```

### Botón de Éxito
```html
<a href="/crear" class="btn btn-success">Crear Nuevo</a>
```

### Alerta de Error
```html
<div class="alert alert-error">
  Ha ocurrido un error al procesar la solicitud
</div>
```

### Tarjeta de Hecho
```html
<div class="hecho-card">
  <h3>Título del Hecho</h3>
  <p>Descripción del hecho...</p>
</div>
```

### Formulario con Grid
```html
<form class="search-form">
  <div class="form-grid">
    <div class="form-group">
      <label>Campo 1</label>
      <input type="text" name="campo1">
    </div>
    <div class="form-group">
      <label>Campo 2</label>
      <input type="text" name="campo2">
    </div>
  </div>
  <button type="submit" class="btn-search">Buscar</button>
</form>
```

### Vista Dual (Lista/Mapa)
```html
<!-- Controles de vista -->
<div class="view-controls">
  <button class="btn-view active" data-view="lista" onclick="cambiarVista('lista')">
    📋 Lista
  </button>
  <button class="btn-view" data-view="mapa" onclick="cambiarVista('mapa')">
    🗺️ Mapa
  </button>
</div>

<!-- Vista de Lista -->
<div id="vista-lista" class="view-container active">
  <div class="hechos-grid">
    <!-- Tarjetas de hechos -->
  </div>
</div>

<!-- Vista de Mapa -->
<div id="vista-mapa" class="view-container">
  <div id="map"></div>
</div>
```

### JavaScript para Cambiar Vistas
```javascript
function cambiarVista(vista) {
  // Actualizar botones activos
  document.querySelectorAll('.btn-view').forEach(btn => {
    btn.classList.remove('active');
  });
  document.querySelector(`[data-view="${vista}"]`).classList.add('active');

  // Cambiar contenedores
  document.querySelectorAll('.view-container').forEach(container => {
    container.classList.remove('active');
  });
  
  if (vista === 'lista') {
    document.getElementById('vista-lista').classList.add('active');
  } else if (vista === 'mapa') {
    document.getElementById('vista-mapa').classList.add('active');
    inicializarMapa(); // Inicializar mapa solo cuando se necesite
  }
}
```

---

## ✅ Mejoras Implementadas

1. ✅ **Creado `style.css`** - Archivo base que faltaba con estilos globales completos
2. ✅ **Actualizado `main.css`** - Estilos modernos para búsqueda y home
3. ✅ **Actualizado `creacion.css`** - Estilos completos para formularios de creación
4. ✅ **Creado `busqueda-hechos.css`** - Sistema de vista dual (lista/mapa) con controles de alternancia
5. ✅ **Creado `mapa-hechos.css`** - Estilos especializados para vista de mapa dedicada
6. ✅ **Actualizado `dashboard.css`** - Sistema completo para el dashboard administrativo
7. ✅ **Vista dual interactiva** - Permite cambiar entre lista de tarjetas y mapa sin recargar
8. ✅ **Integración con Leaflet** - Mapa interactivo con marcadores y popups personalizados
9. ✅ **Consistencia visual** - Paleta de colores unificada en todas las vistas
10. ✅ **Efectos modernos** - Hover, focus, animations y transiciones suaves
11. ✅ **Responsive design** - Funciona perfectamente en todos los dispositivos
12. ✅ **Actualizado vistas HTML** - Eliminado estilos inline, usando clases CSS
13. ✅ **Documentación actualizada** - ESTILOS_CSS.md con ejemplos y guías de uso

---

## 🚀 Próximos Pasos Recomendados

- [ ] Revisar todas las vistas restantes para eliminar estilos inline
- [ ] Agregar iconos con Font Awesome o similar
- [ ] Implementar filtros en tiempo real en la búsqueda
- [ ] Agregar clustering de marcadores para grandes cantidades de datos
- [ ] Implementar modo oscuro (opcional)
- [ ] Agregar más animaciones de transición entre páginas

---

**Fecha de actualización:** 2025-11-07  
**Estado:** ✅ Completado - Incluye vista dual (Lista/Mapa)

