package org.seckill.web;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Stock;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/seckill") // url:/模块/资源/{id}/细分
public class SeckillController {

    @Autowired
    private SeckillService seckillService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value="/list", method= RequestMethod.GET)
    public String list(Model model) {
        // 获取列表页
        List<Stock> seckillList = seckillService.getSeckillList();

        // list.jsp+model = ModelAndView
        model.addAttribute("list",seckillList);

        return "list";
    }

    @GetMapping("/{seckillId}/detail")
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {

        if (seckillId == null) {
            return "forward:/seckill/list";
        }

        Stock seckill = seckillService.getById(seckillId);

        if (seckill == null) {
            return "redirect:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    @PostMapping(value ="/{seckillId}/exposer", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
        SeckillResult<Exposer> result;

        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true,exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            result = new SeckillResult<Exposer>(false,e.getMessage());
        }

        return result;
    }

    @PostMapping(value = {"{seckillId}/{md5}/execution"},produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @CookieValue(value = "killPhone",required = false) Long phone,
                                                   @PathVariable("md5") String md5) {
        if (phone == null) {
            return new SeckillResult<SeckillExecution>(false, SeckillStateEnum.UNREGISTER.getStateInfo());
        }

        SeckillResult<SeckillExecution> result;

        try {
            SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, phone, md5);
            return new SeckillResult<SeckillExecution>(true,seckillExecution);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new SeckillResult<SeckillExecution>(true,e.getMessage());
        }

    }

    @GetMapping("/time/now")
    @ResponseBody
    public SeckillResult<Long> time() {
        return new SeckillResult<Long>(true,new Date().getTime());
    }


}


