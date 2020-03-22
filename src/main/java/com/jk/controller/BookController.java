package com.jk.controller;

import com.jk.config.BookRepostory;
import com.jk.model.Book;
import com.jk.service.BookService;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("book")
public class BookController {

    @Autowired
    private BookRepostory bookRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private BookService bookService;






    @RequestMapping("toBookList")
    public String toBookList(){
        return "bookList";
    }

    @RequestMapping("toAddBookPage")
    public String toAddBookPage(){

        return "addBook";
    }

    @RequestMapping("addBook")
    @ResponseBody
    public void saveBook(Book book){

        bookService.addBook(book);
        //增es
        bookRepository.save(book);

    }


/**
        * searchType  有四种
     *       1. QUERY_AND_FETCH      搜索最快的  因为它只访问一次 shards   但是搜索结果不精确
     *       2. QUERY_THEN_FETCH
     *       3. DFS_QUERY_AND_FETCH
     *       4. DFS_QUERY_THEN_FETCH 最慢的  因为它可能会访问三次 shards 但是它是 搜索结果最精确的
     * BoolQuery 主要用来构建 组合查询
     *     MatchQuery 查询所有
     *     RangeQuery 范围查找
     *     TermQuery 精确查找
     */
    @RequestMapping("queryBook")
    @ResponseBody
    public Map<String,Object> queryBook(String key,Integer pageNumber, Integer pageSize, Book book){

        Map<String, Object> map = new HashMap<>();


        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
       if(key!=null && !"".equals(key)){
           boolQueryBuilder.must(QueryBuilders.matchQuery("copy",key));
        }
       if(book.getBname()!=null &&!"".equals(book.getBname())){
           boolQueryBuilder.must(QueryBuilders.matchQuery("bname",book.getBname()));
       }
        if(book.getMaxprice()!=null && !"".equals(book.getMinprice()) && book.getMinprice()!=null &&!"".equals(book.getMinprice())){
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").from(book.getMinprice()).to(book.getMaxprice()));
        }else{
            if(book.getMinprice()!=null &&!"".equals(book.getMinprice())){
                boolQueryBuilder.must(QueryBuilders.rangeQuery("price").gte(book.getMinprice()));
            }
            if(book.getMaxprice()!=null && !"".equals(book.getMaxprice())){
                boolQueryBuilder.must(QueryBuilders.rangeQuery("price").lte(book.getMaxprice()));
            }
        }

        //获取高亮组件
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("bname");
        highlightBuilder.preTags("<span style='color:red'>");//前缀
        highlightBuilder.postTags("</span>");//后缀

        //设置查询策略
        SearchRequestBuilder searchRequestBuilder = elasticsearchTemplate.getClient().prepareSearch("book2index")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setTypes("book")//设置表名
                .setExplain(true)//设置是否对相关度排序
                .highlighter(highlightBuilder)//设置高亮策略
                .setQuery(boolQueryBuilder)//设置查询策略
                .addSort("price", SortOrder.DESC)//设置排序策略，这里是对价格进行倒序排序
                .setFrom((pageNumber-1)*pageSize) //分页
                .setSize(pageSize);

        //获取响应体
        SearchResponse searchResponse = searchRequestBuilder.get();
        SearchHits hits = searchResponse.getHits();

        //获取总条数
        long totalHits = hits.getTotalHits();
        //放入查询的数据
        List<Book> books = new ArrayList<>();
        //获取数据存放的组件
        SearchHit[] hits1 = hits.getHits();
        for(SearchHit hit:hits1){
            Book b = new Book();
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField title = highlightFields.get("bname");
            //处理高亮字段
            if(title==null){//如果是空就用普通字段代替
                b.setBname(hit.getSourceAsMap().get("bname").toString());
            }else{//如果非空就用高亮字段替换掉查询出的字段
                b.setBname(title.fragments()[0].toString());
            }
            //依次将查询出的其它字段放入对象中
            b.setBname(hit.getSourceAsMap().get("bname").toString());
            b.setId((String) hit.getSourceAsMap().get("id"));
            b.setPrice((Integer) hit.getSourceAsMap().get("price"));
            b.setBjianjie(hit.getSourceAsMap().get("bjianjie").toString());
            b.setBtype((Integer) hit.getSourceAsMap().get("btype"));
            books.add(b);
        }
        map.put("total",totalHits);
        map.put("rows",books);

        return map;
    }
}
