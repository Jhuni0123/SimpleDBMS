/* Generated By:JavaCC: Do not edit this line. MyDBMSParser.java */
import java.util.ArrayList;
import jnDB.*;
import jnDB.type.*;
import jnDB.expression.*;

public class MyDBMSParser implements MyDBMSParserConstants {
  public static final String SYNTAX_ERROR = "Syntax error";
  public static final String PROMPT = "DB_2015-18380> ";

  public static JnDatabase jdb;

  public static void main(String args[]) throws ParseException
  {
    jdb = new JnDatabase();

    jdb.open("db/", "myDatabase");

    MyDBMSParser parser = new MyDBMSParser(System.in);
    System.out.print(PROMPT);

    while (true){
      try {
        parser.command();
        break;
      }
      catch (RuntimeException e) {
        printMessage(e.getMessage());
      }
      catch (Throwable e) {
        printMessage(SYNTAX_ERROR);
      }
      finally {
        MyDBMSParser.ReInit(System.in);
      }
    }

        jdb.close();
  }

  public static void printMessage(String s) {
    System.out.println(s);
    System.out.print(PROMPT);
  }

  static final public void command() throws ParseException {
    queryList();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case EXIT:
      jj_consume_token(EXIT);
      jj_consume_token(SEMICOLON);
      break;
    case 0:
      jj_consume_token(0);
      break;
    default:
      jj_la1[0] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void queryList() throws ParseException {
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case CREATE:
      case DROP:
      case DESC:
      case SHOW:
      case SELECT:
      case INSERT:
      case DELETE:
        ;
        break;
      default:
        jj_la1[1] = jj_gen;
        break label_1;
      }
      query();
      System.out.print(PROMPT);
    }
  }

  static final public void query() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case CREATE:
      createTableQuery();
      break;
    case DROP:
      dropTableQuery();
      break;
    case DESC:
      descQuery();
      break;
    case SHOW:
      showTablesQuery();
      break;
    case SELECT:
      selectQuery();
      break;
    case INSERT:
      insertQuery();
      break;
    case DELETE:
      deleteQuery();
      break;
    default:
      jj_la1[2] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void createTableQuery() throws ParseException {
  String tName;
  TableSchema schema;
    jj_consume_token(CREATE);
    jj_consume_token(TABLE);
    tName = tableName();
    schema = new TableSchema(tName);
    tableElementList(schema);
    jj_consume_token(SEMICOLON);
    jdb.createTable(schema);
  }

  static final public void dropTableQuery() throws ParseException {
  String tName;
    jj_consume_token(DROP);
    jj_consume_token(TABLE);
    tName = tableName();
    jj_consume_token(SEMICOLON);
    jdb.dropTable(tName);
  }

  static final public void descQuery() throws ParseException {
  String tName;
    jj_consume_token(DESC);
    tName = tableName();
    jj_consume_token(SEMICOLON);
    jdb.desc(tName);
  }

  static final public void showTablesQuery() throws ParseException {
    jj_consume_token(SHOW);
    jj_consume_token(TABLES);
    jj_consume_token(SEMICOLON);
    jdb.showTables();
  }

  static final public void selectQuery() throws ParseException {
  ArrayList<Pair<Pair<String,String>,String>> selects;
  Pair<ArrayList<Pair<String,String>>, BooleanExpression> pab;
    jj_consume_token(SELECT);
    selects = selectList();
    pab = tableExpression();
    jj_consume_token(SEMICOLON);
    jdb.select(selects, pab.first, pab.second);
  }

  static final public void insertQuery() throws ParseException {
  String tName;
  Pair<ArrayList<String>, ArrayList<Value>> listPair;
    jj_consume_token(INSERT);
    jj_consume_token(INTO);
    tName = tableName();
    listPair = insertColumnAndSource();
    jj_consume_token(SEMICOLON);
    jdb.insert(tName, listPair.first, listPair.second);
  }

  static final public void deleteQuery() throws ParseException {
  String tName;
  BooleanExpression bexp = new BooleanExpression(true);
    jj_consume_token(DELETE);
    jj_consume_token(FROM);
    tName = tableName();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case WHERE:
      bexp = whereClause();
      break;
    default:
      jj_la1[3] = jj_gen;
      ;
    }
    jj_consume_token(SEMICOLON);
    jdb.delete(tName, bexp);
  }

  static final public void tableElementList(TableSchema schema) throws ParseException {
    jj_consume_token(LEFT_PAREN);
    tableElement(schema);
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        jj_la1[4] = jj_gen;
        break label_2;
      }
      jj_consume_token(COMMA);
      tableElement(schema);
    }
    jj_consume_token(RIGHT_PAREN);
  }

  static final public void tableElement(TableSchema schema) throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LEGAL_IDENTIFIER:
      columnDefinition(schema);
      break;
    case PRIMARY:
    case FOREIGN:
      tableConstraintDefinition(schema);
      break;
    default:
      jj_la1[5] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void columnDefinition(TableSchema schema) throws ParseException {
  String cName;
  Type type;
  boolean isNotNull = false;
    cName = columnName();
    type = dataType();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NOT:
      jj_consume_token(NOT);
      jj_consume_token(NULL);
      isNotNull = true;
      break;
    default:
      jj_la1[6] = jj_gen;
      ;
    }
    schema.addColumn(cName, type, isNotNull);
  }

  static final public void tableConstraintDefinition(TableSchema schema) throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case PRIMARY:
      primaryKeyConstraint(schema);
      break;
    case FOREIGN:
      referentialConstraint(schema);
      break;
    default:
      jj_la1[7] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void primaryKeyConstraint(TableSchema schema) throws ParseException {
  ArrayList<String> cnList;
    jj_consume_token(PRIMARY);
    jj_consume_token(KEY);
    cnList = columnNameList();
    schema.setPrimaryKey(cnList);
  }

  static final public void referentialConstraint(TableSchema schema) throws ParseException {
  String tName;
  ArrayList<String> cnList1,cnList2;
  Table table;
    jj_consume_token(FOREIGN);
    jj_consume_token(KEY);
    cnList1 = columnNameList();
    jj_consume_token(REFERENCES);
    tName = tableName();
    cnList2 = columnNameList();
    table = jdb.checkPrimaryKey(tName,cnList2);
    schema.addReferentialKey(cnList1, table, cnList2);
  }

  static final public ArrayList<Pair<Pair<String,String>,String>> selectList() throws ParseException {
  ArrayList<Pair<Pair<String,String>,String>> selects = new ArrayList<Pair<Pair<String,String>,String>>();
  Pair<Pair<String,String>,String> select;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ASTERISK:
      jj_consume_token(ASTERISK);
      break;
    case LEGAL_IDENTIFIER:
      select = selectedColumn();
        selects.add(select);
      label_3:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMA:
          ;
          break;
        default:
          jj_la1[8] = jj_gen;
          break label_3;
        }
        jj_consume_token(COMMA);
        select = selectedColumn();
          selects.add(select);
      }
      break;
    default:
      jj_la1[9] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    {if (true) return selects;}
    throw new Error("Missing return statement in function");
  }

  static final public Pair<Pair<String,String>,String> selectedColumn() throws ParseException {
  String tName= null, cName, cNameAs = null;
    if (jj_2_1(2)) {
      tName = tableName();
      jj_consume_token(PERIOD);
    } else {
      ;
    }
    cName = columnName();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case AS:
      jj_consume_token(AS);
      cNameAs = columnName();
      break;
    default:
      jj_la1[10] = jj_gen;
      ;
    }
    {if (true) return new Pair<Pair<String,String>,String>(new Pair<String,String>(tName,cName),cNameAs);}
    throw new Error("Missing return statement in function");
  }

  static final public Pair<ArrayList<Pair<String,String>>, BooleanExpression> tableExpression() throws ParseException {
  ArrayList<Pair<String,String>> tRefList;
  BooleanExpression bexp = new BooleanExpression(true);
    tRefList = fromClause();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case WHERE:
      bexp = whereClause();
      break;
    default:
      jj_la1[11] = jj_gen;
      ;
    }
    {if (true) return new Pair<ArrayList<Pair<String,String>>, BooleanExpression>(tRefList,bexp);}
    throw new Error("Missing return statement in function");
  }

  static final public ArrayList<Pair<String,String>> fromClause() throws ParseException {
  ArrayList<Pair<String,String>> tRefList;
    jj_consume_token(FROM);
    tRefList = tableReferenceList();
    {if (true) return tRefList;}
    throw new Error("Missing return statement in function");
  }

  static final public BooleanExpression whereClause() throws ParseException {
  BooleanExpression bexp;
    jj_consume_token(WHERE);
    bexp = booleanValueExpression();
    {if (true) return bexp;}
    throw new Error("Missing return statement in function");
  }

  static final public ArrayList<Pair<String,String>> tableReferenceList() throws ParseException {
  ArrayList<Pair<String,String>> tRefList = new ArrayList<Pair<String,String>>();
  Pair<String,String> tRef;
    tRef = referedTable();
    tRefList.add(tRef);
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        jj_la1[12] = jj_gen;
        break label_4;
      }
      jj_consume_token(COMMA);
      tRef = referedTable();
      tRefList.add(tRef);
    }
    {if (true) return tRefList;}
    throw new Error("Missing return statement in function");
  }

  static final public Pair<String,String> referedTable() throws ParseException {
  String tName, tNameAs = null;
    tName = tableName();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case AS:
      jj_consume_token(AS);
      tNameAs = tableName();
      break;
    default:
      jj_la1[13] = jj_gen;
      ;
    }
    {if (true) return new Pair<String,String >(tName, tNameAs);}
    throw new Error("Missing return statement in function");
  }

  static final public BooleanExpression booleanValueExpression() throws ParseException {
  ArrayList<BooleanTerm> bterms = new ArrayList<BooleanTerm>();
  BooleanTerm bterm;
    // if we choose booleanValueExpression() < OR > booleanTerm() , left recursion can occur(infinite loop).
      // so we can change it and write like below.
       bterm = booleanTerm();
     bterms.add(bterm);
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case OR:
        ;
        break;
      default:
        jj_la1[14] = jj_gen;
        break label_5;
      }
      jj_consume_token(OR);
      booleanTerm();
      bterms.add(bterm);
    }
    {if (true) return new BooleanExpression(bterms);}
    throw new Error("Missing return statement in function");
  }

  static final public BooleanTerm booleanTerm() throws ParseException {
  ArrayList<BooleanFactor> bFactors = new ArrayList<BooleanFactor>();
  BooleanFactor bFactor;
    bFactor = booleanFactor();
    bFactors.add(bFactor);
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case AND:
        ;
        break;
      default:
        jj_la1[15] = jj_gen;
        break label_6;
      }
      jj_consume_token(AND);
      booleanFactor();
      bFactors.add(bFactor);
    }
    {if (true) return new BooleanTerm(bFactors);}
    throw new Error("Missing return statement in function");
  }

  static final public BooleanFactor booleanFactor() throws ParseException {
  boolean not = false;
  BooleanTest bTest;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NOT:
      jj_consume_token(NOT);
      not = true;
      break;
    default:
      jj_la1[16] = jj_gen;
      ;
    }
    bTest = booleanTest();
    {if (true) return new BooleanFactor(not, bTest);}
    throw new Error("Missing return statement in function");
  }

  static final public BooleanTest booleanTest() throws ParseException {
  BooleanTest bTest;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case CHAR_STRING:
    case INT_VALUE:
    case DATE_VALUE:
    case LEGAL_IDENTIFIER:
      bTest = predicate();
      break;
    case LEFT_PAREN:
      bTest = parenthesizedBooleanExpression();
      break;
    default:
      jj_la1[17] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    {if (true) return bTest;}
    throw new Error("Missing return statement in function");
  }

  static final public ParenthesizedBooleanExpression parenthesizedBooleanExpression() throws ParseException {
  BooleanExpression bexp;
    jj_consume_token(LEFT_PAREN);
    bexp = booleanValueExpression();
    jj_consume_token(RIGHT_PAREN);
    {if (true) return new ParenthesizedBooleanExpression(bexp);}
    throw new Error("Missing return statement in function");
  }

  static final public Predicate predicate() throws ParseException {
  Predicate pred;
    if (jj_2_2(4)) {
      pred = comparisonPredicate();
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LEGAL_IDENTIFIER:
        pred = nullPredicate();
        break;
      default:
        jj_la1[18] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    {if (true) return pred;}
    throw new Error("Missing return statement in function");
  }

  static final public NullPredicate nullPredicate() throws ParseException {
  String tName = null,cName;
  boolean isNull;
    if (jj_2_3(2)) {
      tName = tableName();
      jj_consume_token(PERIOD);
    } else {
      ;
    }
    cName = columnName();
    isNull = nullOperation();
    {if (true) return new NullPredicate(tName, cName, isNull);}
    throw new Error("Missing return statement in function");
  }

  static final public boolean nullOperation() throws ParseException {
  boolean isNull = true;
    jj_consume_token(IS);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NOT:
      jj_consume_token(NOT);
      isNull = false;
      break;
    default:
      jj_la1[19] = jj_gen;
      ;
    }
    jj_consume_token(NULL);
    {if (true) return isNull;}
    throw new Error("Missing return statement in function");
  }

  static final public ComparisonPredicate comparisonPredicate() throws ParseException {
  CompOperand l,r;
  Token op;
    l = compOperand();
    op = jj_consume_token(COMP_OP);
    r = compOperand();
    {if (true) return new ComparisonPredicate(l, op.toString(), r);}
    throw new Error("Missing return statement in function");
  }

  static final public CompOperand compOperand() throws ParseException {
  CompOperand cop;
  Value value;
  String tName = null, cName;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case CHAR_STRING:
    case INT_VALUE:
    case DATE_VALUE:
      value = comparableValue();
        cop = new ComparableValue(value);
      break;
    case LEGAL_IDENTIFIER:
      if (jj_2_4(2)) {
        tName = tableName();
        jj_consume_token(PERIOD);
      } else {
        ;
      }
      cName = columnName();
        cop = new ColumnValue(tName, cName);
      break;
    default:
      jj_la1[20] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    {if (true) return cop;}
    throw new Error("Missing return statement in function");
  }

  static final public Pair<ArrayList<String>, ArrayList<Value>> insertColumnAndSource() throws ParseException {
  ArrayList<String> cnList = null;
  ArrayList<Value> vList;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LEFT_PAREN:
      cnList = columnNameList();
      break;
    default:
      jj_la1[21] = jj_gen;
      ;
    }
    vList = valueList();
    {if (true) return new Pair<ArrayList<String>, ArrayList<Value>>(cnList,vList);}
    throw new Error("Missing return statement in function");
  }

  static final public ArrayList<String> columnNameList() throws ParseException {
  ArrayList<String> cnList = new ArrayList<String>();
  String cName;
    jj_consume_token(LEFT_PAREN);
    cName = columnName();
    cnList.add(cName);
    label_7:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        jj_la1[22] = jj_gen;
        break label_7;
      }
      jj_consume_token(COMMA);
      cName = columnName();
      cnList.add(cName);
    }
    jj_consume_token(RIGHT_PAREN);
    {if (true) return cnList;}
    throw new Error("Missing return statement in function");
  }

  static final public ArrayList<Value> valueList() throws ParseException {
  ArrayList<Value> vList = new ArrayList<Value>();
  Value v;
    jj_consume_token(VALUES);
    jj_consume_token(LEFT_PAREN);
    v = value();
    vList.add(v);
    label_8:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        jj_la1[23] = jj_gen;
        break label_8;
      }
      jj_consume_token(COMMA);
      v = value();
      vList.add(v);
    }
    jj_consume_token(RIGHT_PAREN);
    {if (true) return vList;}
    throw new Error("Missing return statement in function");
  }

  static final public Value value() throws ParseException {
  Value value;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NULL:
      jj_consume_token(NULL);
         value = new NullValue();
      break;
    case CHAR_STRING:
    case INT_VALUE:
    case DATE_VALUE:
      value = comparableValue();
      break;
    default:
      jj_la1[24] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
      {if (true) return value;}
    throw new Error("Missing return statement in function");
  }

  static final public Value comparableValue() throws ParseException {
  Token token;
  Value value;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INT_VALUE:
      token = jj_consume_token(INT_VALUE);
        value = new IntValue(Integer.parseInt(token.toString()));
      break;
    case CHAR_STRING:
      token = jj_consume_token(CHAR_STRING);
        String str = token.toString();
        value = new CharValue(str.substring(1,str.length()-1));
      break;
    case DATE_VALUE:
      token = jj_consume_token(DATE_VALUE);
        value = new DateValue(token.toString());
      break;
    default:
      jj_la1[25] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
      {if (true) return value;}
    throw new Error("Missing return statement in function");
  }

  static final public Type dataType() throws ParseException {
  Token l;
  Type type;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INT:
      jj_consume_token(INT);
        type = new IntType();
      break;
    case CHAR:
      jj_consume_token(CHAR);
      jj_consume_token(LEFT_PAREN);
      l = jj_consume_token(INT_VALUE);
      jj_consume_token(RIGHT_PAREN);
        type = new CharType(Integer.parseInt(l.toString()));
      break;
    case DATE:
      jj_consume_token(DATE);
        type = new DateType();
      break;
    default:
      jj_la1[26] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
      {if (true) return type;}
    throw new Error("Missing return statement in function");
  }

  static final public String tableName() throws ParseException {
  Token l_id;
    l_id = jj_consume_token(LEGAL_IDENTIFIER);
    {if (true) return l_id.toString().toLowerCase();}
    throw new Error("Missing return statement in function");
  }

  static final public String columnName() throws ParseException {
  Token l_id;
    l_id = jj_consume_token(LEGAL_IDENTIFIER);
    {if (true) return l_id.toString().toLowerCase();}
    throw new Error("Missing return statement in function");
  }

  static private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  static private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  static private boolean jj_2_3(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_3(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(2, xla); }
  }

  static private boolean jj_2_4(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_4(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(3, xla); }
  }

  static private boolean jj_3R_15() {
    if (jj_scan_token(LEGAL_IDENTIFIER)) return true;
    return false;
  }

  static private boolean jj_3R_10() {
    if (jj_3R_11()) return true;
    if (jj_scan_token(COMP_OP)) return true;
    if (jj_3R_11()) return true;
    return false;
  }

  static private boolean jj_3R_13() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_4()) jj_scanpos = xsp;
    if (jj_3R_15()) return true;
    return false;
  }

  static private boolean jj_3R_17() {
    if (jj_scan_token(CHAR_STRING)) return true;
    return false;
  }

  static private boolean jj_3_3() {
    if (jj_3R_9()) return true;
    if (jj_scan_token(PERIOD)) return true;
    return false;
  }

  static private boolean jj_3_1() {
    if (jj_3R_9()) return true;
    if (jj_scan_token(PERIOD)) return true;
    return false;
  }

  static private boolean jj_3R_12() {
    if (jj_3R_14()) return true;
    return false;
  }

  static private boolean jj_3R_16() {
    if (jj_scan_token(INT_VALUE)) return true;
    return false;
  }

  static private boolean jj_3R_9() {
    if (jj_scan_token(LEGAL_IDENTIFIER)) return true;
    return false;
  }

  static private boolean jj_3R_11() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_12()) {
    jj_scanpos = xsp;
    if (jj_3R_13()) return true;
    }
    return false;
  }

  static private boolean jj_3R_14() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3R_16()) {
    jj_scanpos = xsp;
    if (jj_3R_17()) {
    jj_scanpos = xsp;
    if (jj_3R_18()) return true;
    }
    }
    return false;
  }

  static private boolean jj_3_2() {
    if (jj_3R_10()) return true;
    return false;
  }

  static private boolean jj_3R_18() {
    if (jj_scan_token(DATE_VALUE)) return true;
    return false;
  }

  static private boolean jj_3_4() {
    if (jj_3R_9()) return true;
    if (jj_scan_token(PERIOD)) return true;
    return false;
  }

  static private boolean jj_initialized_once = false;
  /** Generated Token Manager. */
  static public MyDBMSParserTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  /** Current token. */
  static public Token token;
  /** Next token. */
  static public Token jj_nt;
  static private int jj_ntk;
  static private Token jj_scanpos, jj_lastpos;
  static private int jj_la;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[27];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x21,0xfe00,0xfe00,0x100000,0x0,0x30000000,0x1000000,0x30000000,0x0,0x0,0x400000,0x100000,0x0,0x400000,0x2000000,0x4000000,0x1000000,0x0,0x0,0x1000000,0x0,0x0,0x0,0x0,0x8000000,0x0,0x1c0,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x0,0x0,0x0,0x0,0x800,0x8,0x0,0x0,0x800,0x4008,0x0,0x0,0x800,0x0,0x0,0x0,0x0,0x20f,0x8,0x0,0xf,0x200,0x800,0x800,0x7,0x7,0x0,};
   }
  static final private JJCalls[] jj_2_rtns = new JJCalls[4];
  static private boolean jj_rescan = false;
  static private int jj_gc = 0;

  /** Constructor with InputStream. */
  public MyDBMSParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public MyDBMSParser(java.io.InputStream stream, String encoding) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new MyDBMSParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 27; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 27; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public MyDBMSParser(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new MyDBMSParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 27; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 27; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public MyDBMSParser(MyDBMSParserTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 27; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(MyDBMSParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 27; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  static private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error { }
  static final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  static private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }


/** Get the next Token. */
  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  static final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  static private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  static private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  static private int[] jj_expentry;
  static private int jj_kind = -1;
  static private int[] jj_lasttokens = new int[100];
  static private int jj_endpos;

  static private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      jj_entries_loop: for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
        int[] oldentry = (int[])(it.next());
        if (oldentry.length == jj_expentry.length) {
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              continue jj_entries_loop;
            }
          }
          jj_expentries.add(jj_expentry);
          break jj_entries_loop;
        }
      }
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  /** Generate ParseException. */
  static public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[51];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 27; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 51; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  static final public void enable_tracing() {
  }

  /** Disable tracing. */
  static final public void disable_tracing() {
  }

  static private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 4; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
            case 2: jj_3_3(); break;
            case 3: jj_3_4(); break;
          }
        }
        p = p.next;
      } while (p != null);
      } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  static private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}
