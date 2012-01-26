

package rwccompiler;

import java.util.*;

public class SymTab {

/*
insert to front of list
remove from front of list
iterate over list
access first element in list

*/
  private List<Map<String, Sym>> table;

  public SymTab() {
    this.table = new LinkedList<HashMap<String, Sym>>();

    HashMap<String, Sym> newMap = new HashMap<String, Sym>();
    this.table.add(newMap);
  }

  public void insert(String name, Sym sym)
      throws DuplicateException, EmptySymTabException {

    if (this.table.size() == 0) {
      throw EmptySymTabException;
    }
    
    map = table.get(0);

    if (map.containsKey(name)) {
      throw DuplicateException;
    }
    
    map.put(name, sym);
  }

}

