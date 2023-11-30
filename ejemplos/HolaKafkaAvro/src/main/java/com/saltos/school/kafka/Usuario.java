package com.saltos.school.kafka;

public class Usuario {
  
  private String nombre;

  private String apellido;

  private Integer edad;

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getApellido() {
    return apellido;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  public Integer getEdad() {
    return edad;
  }

  public void setEdad(Integer edad) {
    this.edad = edad;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
    result = prime * result + ((apellido == null) ? 0 : apellido.hashCode());
    result = prime * result + ((edad == null) ? 0 : edad.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Usuario other = (Usuario) obj;
    if (nombre == null) {
      if (other.nombre != null)
        return false;
    } else if (!nombre.equals(other.nombre))
      return false;
    if (apellido == null) {
      if (other.apellido != null)
        return false;
    } else if (!apellido.equals(other.apellido))
      return false;
    if (edad == null) {
      if (other.edad != null)
        return false;
    } else if (!edad.equals(other.edad))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Usuario [nombre=" + nombre + ", apellido=" + apellido + ", edad=" + edad + "]";
  }

  
}
