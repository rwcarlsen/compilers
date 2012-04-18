// **********************************************************************
// The SymTab class implements a symbol table, which is a List
// of Maps, each containing zero or more Syms.
//
// Public Methods
// ==============
//
// constructor
// -----------
// SymTab()            -- initialize the table to be empty (i.e., to
//                        be a List containing one, empty Map)
//
// modifiers
// ---------
// void insert(String name, Sym sym)
//                       -- add name and sym to the first Map in the List
//                          (error if the List is empty or if it
//                          already has name as a key)
//
// void addMap()         -- add a new, empty Map to the front of
//                          the List
//
// void removeMap()      -- remove the Map from the front of
//                          the List (error if the List is
//                          already empty)
//
// other operations
// ----------------
// Sym localLookup(String k)
//                       -- if the first Map in the List has key k,
//                          then return the associated Sym;
//                          otherwise, return null
//
// Sym globalLookup(String k)
//                       -- if some Map in the List has key k,
//                          then return the associated Sym;
//                          otherwise, return null
//
// void print()          -- print all of the Maps in the List,
//                          one per line, to stdout


import java.io.*;
import java.util.*;

class SymTab {
  private FnSym front;
  private List<HashMap<String,Sym>> myList; // the List of Maps

  public FnSym frontFunc() {
    return this.front;
  }

  private void setFront(Sym sym) {
    if (sym.isFunc()) {
      this.front = (FnSym)sym;
    }
  }

  // **********************************************************************
  // constructor
  // **********************************************************************
  public SymTab() {
    this.front = null;
    myList = new LinkedList<HashMap<String,Sym>>();
    addMap();
  }

  // **********************************************************************
  // insert
  // **********************************************************************
  public void insert(String name, Sym sym)
    throws DuplicateException, EmptySymTabException
    {
      if (myList.size() == 0) throw new EmptySymTabException();
      if (localLookup(name) != null) throw new DuplicateException();
      HashMap<String,Sym> oneMap = myList.get(0);
      oneMap.put(name, sym);
      setFront(sym);
    }

  // **********************************************************************
  // addMap
  // **********************************************************************
  public void addMap() {
    myList.add(0, new HashMap<String,Sym>());
  }

  // **********************************************************************
  // removeMap
  // **********************************************************************
  public void removeMap() throws EmptySymTabException {
    if (myList.isEmpty()) {
      throw new EmptySymTabException();
    }
    myList.remove(0);
    this.front = null;
  }

  // **********************************************************************
  // localLookup
  // **********************************************************************
  public Sym localLookup(String str) {
    if (myList.size() == 0) return null;
    Sym tmp;
    HashMap<String,Sym> oneMap = myList.get(0);
    tmp = oneMap.get(str);
    if (tmp != null) return tmp;
    return null;
  }

  // **********************************************************************
  // globalLookup
  // **********************************************************************
  public Sym globalLookup(String str) {
    if (myList.size() == 0) return null;
    Sym tmp;
    for (HashMap<String,Sym> oneMap : myList) {
      tmp = oneMap.get(str);
      if (tmp != null) return tmp;
    }
    return null;
  }

  // **********************************************************************
  // print
  // **********************************************************************
  public void print() {
    System.out.println("\nSYMBOL TABLE");
    for (HashMap<String,Sym> oneMap : myList) {
      System.out.println(oneMap.toString());
    }
    System.out.println();
  }
}
