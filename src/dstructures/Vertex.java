package dstructures;
import java.io.IOException;
import java.io.Serializable;

public class Vertex implements Serializable {

    private static final long serialVersionUID = -6317852784750296025L;
    public Integer id;

    public Vertex(int id) {
	this.id = id;
    }

    public int getId() {
	return id;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((id == null) ? 0 : id.hashCode());
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
	Vertex other = (Vertex) obj;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "V<" + id + ">";
    }
    private void writeObject(java.io.ObjectOutputStream stream)
            throws IOException {
        stream.writeInt(id);
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        id = stream.readInt();
    }

}