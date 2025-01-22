package com.test.back.hesi.web.model.cmmn;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BfListResponse<T>{
    private List<T> list = new ArrayList<T>();;
    private long count = 0;
    
    private Page page;
    
    class Page{
        private long totalDataCnt = 0;
        private long totalPages = 0;
        private boolean isLastPage = false;
        private boolean isFirstPage = false;
        private long requestPage = 0;
        private long requestSize = 0;
        
        public long getTotalDataCnt() {
            return totalDataCnt;
        }

        public void setTotalDataCnt(long totalDataCnt) {
            this.totalDataCnt = totalDataCnt;
        }

        public long getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(long totalPages) {
            this.totalPages = totalPages;
        }

        public boolean isLastPage() {
            return isLastPage;
        }

        public void setLastPage(boolean isLastPage) {
            this.isLastPage = isLastPage;
        }

        public boolean isFirstPage() {
            return isFirstPage;
        }

        public void setFirstPage(boolean isFirstPage) {
            this.isFirstPage = isFirstPage;
        }

        public long getRequestPage() {
            return requestPage;
        }

        public void setRequestPage(long requestPage) {
            this.requestPage = requestPage;
        }

        public long getRequestSize() {
            return requestSize;
        }

        public void setRequestSize(long requestSize) {
            this.requestSize = requestSize;
        }

        Page(BfPage bfPage){
            totalDataCnt = count;
            totalPages = count / bfPage.getPageSize() + (count % bfPage.getPageSize() == 0 ? 0 : 1);
            requestPage = bfPage.getPageNo();
            requestSize = bfPage.getPageSize();
            isFirstPage = bfPage.getPageNo() == 1;
            isLastPage = bfPage.getPageNo() == totalPages;
        }
    }
    
    public BfListResponse(List<T> list, long count, BfPage bfPage) {
        this.list = list;
        this.count = count;
        
        this.page = new Page(bfPage);
    }
}
