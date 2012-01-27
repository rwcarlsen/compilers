
package rwccompiler;

import java.util.*;

public class SymTab {

  private List<Map<String, Sym>> table;

  public SymTab() {
    this.table = new LinkedList<Map<String, Sym>>();

    Map<String, Sym> newMap = new HashMap<String, Sym>();
    this.table.add(newMap);
  }

  public void removeMap() throws EmptySymTabException {
    if (this.table.size() == 0) {
      throw new EmptySymTabException();
    }

    this.table.remove(0);
  }

  public void addMap() {
    Map<String, Sym> newMap = new HashMap<String, Sym>();
    this.table.add(0, newMap);
  }

  public void insert(String name, Sym sym)
      throws DuplicateException, EmptySymTabException {

    if (this.table.size() == 0) {
      throw new EmptySymTabException();
    }
    
    Map<String, Sym> map = table.get(0);

    if (map.containsKey(name)) {
      throw new DuplicateException();
    }
    
    map.put(name, sym);
  }

  public Sym localLookup(String name) {
    if (this.table.size() == 0) {
      return null;
    }

    Map<String, Sym> map = table.get(0);
    if (!map.containsKey(name)) {
      return null;
    }
    return map.get(name);
  }

  public Sym globalLookup(String name) {
    if (this.table.size() == 0) {
      return null;
    }

    for (Map<String, Sym> map : this.table) {
      if (map.containsKey(name)) {
        return map.get(name);
      }
    }
    return null;
  }

  public void print() {
    System.out.print("\nSYMBOL TABLE\n");
    for (Map<String, Sym> map : this.table) {
      System.out.println(map.toString());
    }
  }

}

