package nardi.francisco.juan.passazo;

public class ConsultaItem {


            String token;
            String fila;
            String dni;
            String nombre;
            String cantidad;
            String estado;
            String qr_code;
            String url;
            String observaciones;
            String evento;
            String fecha;

    public ConsultaItem(String token, String fila, String dni, String nombre, String cantidad, String estado, String qr_code, String url, String observaciones, String evento, String fecha) {
        this.token = token;
        this.fila = fila;
        this.dni = dni;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.estado = estado;
        this.qr_code = qr_code;
        this.url = url;
        this.observaciones = observaciones;
        this.evento = evento;
        this.fecha = fecha;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFila() {
        return fila;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
