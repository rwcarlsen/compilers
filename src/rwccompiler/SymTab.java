
package rwccompiler;

import java.util.*;

/** Compiler symbol table */
public class SymTab {

  private List<Map<String, Sym>> table;

  /** initialize symbol table with one blank Map */
  public SymTab() {
    this.table = new LinkedList<Map<String, Sym>>();

    Map<String, Sym> newMap = new HashMap<String, Sym>();
    this.table.add(newMap);
  }

  /** Remove (delete) the HashMap from the front of the list.
   *
   * @throws EmptySymTabException the SymTab's list is empty
   */
  public void removeMap() throws EmptySymTabException {
    if (this.table.size() == 0) {
      throw new EmptySymTabException();
    }

    this.table.remove(0);
  }

  /** Add a new, empty HashMap to the front of the list. */
  public void addMap() {
    Map<String, Sym> newMap = new HashMap<String, Sym>();
    this.table.add(0, newMap);
  }

  /** Add the given name and sym to the first HashMap in the list.
   *
   * @throws EmptySymTabException the SymTab's list is empty
   * @throws DuplicateException the first Hashmap in the list already
   *         contains the given name as a key
   */
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

  /** Retrieve the Sym for the given name from the first hashmap in the list.
   *
   *
   * @return If the first HashMap in the list has name as a key, return
   *         the associated Sym. If this SymTab's list is empty or if the
   *         key is not present in the first Map, return null.
   */
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

  /** Retrieve the Sym from the first hashmap that contains the given
   *  name as a key.
   *
   *
   * @return If a HashMap in the list has name as a key, return
   *         the associated Sym from the first HashMap that contains it.
   *         If this SymTab's list is empty or if the key is not present
   *         in any Map, return null.
   */
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

  /** pretty-print the symbol table */
  public void print() {
    System.out.print("\nSYMBOL TABLE\n");
    for (Map<String, Sym> map : this.table) {
      System.out.println(map.toString());
    }
  }
}

