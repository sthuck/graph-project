import java.util.ArrayList;
import java.util.List;

public class Vertex {
  final public Integer id;
  public List<Edge> adjencies; 

  
  
  public Vertex(int id) {
    this.id = id;
    adjencies = new ArrayList<>();
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
    return "V<"+id+">";
  }
  
} 