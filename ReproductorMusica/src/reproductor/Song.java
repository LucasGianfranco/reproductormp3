package reproductor;

public class Song {
	
	private String path;
	private String nombre;
	private int duracion;

	public Song(String path, String nombre, int duracion) {
		super();
		this.path = path;
		this.nombre = nombre;
		this.duracion = duracion;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getDuracion() {
		return duracion;
	}
	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}
	@Override
	public String toString() {
		return this.nombre;
	}
}
