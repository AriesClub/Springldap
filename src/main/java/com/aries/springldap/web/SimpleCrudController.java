package com.aries.springldap.web;

import com.aries.springldap.SimpleCrud;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/simplecruds")
@Controller
public class SimpleCrudController {

	@RequestMapping(method = RequestMethod.POST)
    public String create(@Valid SimpleCrud simpleCrud, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("simpleCrud", simpleCrud);
            return "simplecruds/create";
        }
        uiModel.asMap().clear();
        simpleCrud.persist();
        return "redirect:/simplecruds/" + encodeUrlPathSegment(simpleCrud.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(Model uiModel) {
        uiModel.addAttribute("simpleCrud", new SimpleCrud());
        return "simplecruds/create";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("simplecrud", SimpleCrud.findSimpleCrud(id));
        uiModel.addAttribute("itemId", id);
        return "simplecruds/show";
    }

	@RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            uiModel.addAttribute("simplecruds", SimpleCrud.findSimpleCrudEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) SimpleCrud.countSimpleCruds() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("simplecruds", SimpleCrud.findAllSimpleCruds());
        }
        return "simplecruds/list";
    }

	@RequestMapping(method = RequestMethod.PUT)
    public String update(@Valid SimpleCrud simpleCrud, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("simpleCrud", simpleCrud);
            return "simplecruds/update";
        }
        uiModel.asMap().clear();
        simpleCrud.merge();
        return "redirect:/simplecruds/" + encodeUrlPathSegment(simpleCrud.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("simpleCrud", SimpleCrud.findSimpleCrud(id));
        return "simplecruds/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        SimpleCrud.findSimpleCrud(id).remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/simplecruds";
    }

	@ModelAttribute("simplecruds")
    public Collection<SimpleCrud> populateSimpleCruds() {
        return SimpleCrud.findAllSimpleCruds();
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        }
        catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
}
