package ua.demo.util;
/*
    class provides Pagination
 */
public class PagingModel {
    //amount of elements on page
    private final int PAGE_ROWS =2;
    //amount of neighbours of current page in paging line
    //i.e. length of paging line
    private final int N_NUM = 2;
    //amount of pages when we use simple pagination
    private final int SIMPLE_PAG_NUMB=6;
    //amount of all elements
    private int total;
    //last page number
    private int LastPageNum;
    //current page
    private int curPage;
    //offset for sql query for current page
    private int SqlOffset;
    //url for making href in <a>
    private String url="";
    //array of symbols to render paging line
    public String[] symbol;
    //array of page numbers to produce href in <a>
    public int[] page;


    public PagingModel(int total, int curPage,String url) {
        this.total = total;
        this.url=url;
        LastPageNum = (int) Math.ceil(total / (double) PAGE_ROWS);
        //checks correct currentPage value
        if (curPage < 1) curPage = 1;
        else if (curPage > LastPageNum) curPage = LastPageNum;
        this.curPage=curPage;
        //creates arrays `symbol` and `page` for rendering
        createPagination();
        //calculates sql offset
        SqlOffset=1+(curPage-1)*PAGE_ROWS;
        if (SqlOffset<1) SqlOffset=1;
    }

    private void createPagination()
    {
        if(LastPageNum<=SIMPLE_PAG_NUMB)
        {
            page =new int[LastPageNum];
            symbol =new String[LastPageNum];
            for(int i=0;i<LastPageNum;i++)
            {
                page[i]=i+1;
                symbol[i]=String.valueOf(i+1);
            }
        }
        else
        {


            int size=0;
            boolean leftSimple;
            boolean rightSimple;
            int lastPos=0;
            if (curPage-N_NUM<=2)
            {
                size=size+curPage;
                leftSimple=true;
            }
            else
            {
                size=size+N_NUM+3;
                leftSimple=false;
            }


            if (curPage+N_NUM>=LastPageNum-1)
            {
                size=size+LastPageNum-curPage;
                rightSimple=true;
            }
            else
            {
                size=size+N_NUM+2;
                rightSimple=false;
            }



            page =new int[size];
            symbol =new String[size];
            if(leftSimple)
            {
                for (int i=0;i<curPage;i++)
                {
                    symbol[i]=String.valueOf(i+1);
                    page[i]=i+1;
                }
                lastPos=curPage-1;
            }
            else
            {
                symbol[0]="1";
                page[0]=1;
                symbol[1]="...";
                page[1]=curPage-N_NUM-1;

                for (int i=2,j=curPage-N_NUM;j<=curPage;i++,j++)
                {
                    symbol[i]=String.valueOf(j);
                    page[i]=j;
                }
                lastPos=N_NUM+2;

            }

            if(rightSimple)
            {
                for(int i=lastPos+1,j=curPage+1;j<=LastPageNum;i++,j++)
                {
                    symbol[i]=String.valueOf(j);
                    page[i]=j;
                }
            }
            else
            {
                for(int i=lastPos+1,j=curPage+1;i<=lastPos+N_NUM;i++,j++)
                {
                    symbol[i]=String.valueOf(j);
                    page[i]=j;
                }
                page[lastPos+N_NUM+1]=curPage+N_NUM+1;
                symbol[lastPos+N_NUM+1]="...";

                page[lastPos+N_NUM+2]=LastPageNum;
                symbol[lastPos+N_NUM+2]=String.valueOf(LastPageNum);
            }
        }
    }

    //getters
    public String getUrl() {
        return url;
    }

    public int getLastPageNum() {
        return LastPageNum;
    }

    public int getPAGE_ROWS() {
        return PAGE_ROWS;
    }

    public int getCurPage() {
        return curPage;
    }

    public int getSqlOffset() {
        return SqlOffset;
    }

    public String[] getSymbol() {
        return symbol;
    }

    public int[] getPage() {
        return page;
    }
}
