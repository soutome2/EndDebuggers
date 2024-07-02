package com.example.demo.Controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Entity.Customer;
import com.example.demo.Entity.Reserve;
import com.example.demo.Entity.ReserveCustomer;
import com.example.demo.Form.CustomerInputForm;
import com.example.demo.Form.LoginForm;
import com.example.demo.Form.ReserveInputForm;
import com.example.demo.Repository.CustomerRepository;
import com.example.demo.Repository.ReserveCustomerRepository;
import com.example.demo.Repository.ReserveRepository;
import com.example.demo.Service.CustomerService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
public class AceController {
	private final CustomerRepository customerRepository;
	private final ReserveRepository reserveRepository;
	private final ReserveCustomerRepository reserveCustomerRepository;
	private final CustomerService customerService;
	private final HttpSession session;

	@GetMapping("/")
	public String GetHome() {
		return "home";

	}

	@GetMapping("/Login")
	public String GetUserLogin(LoginForm loginForm) {
		return "login";

	}

	@GetMapping("/Reserve")
	public ModelAndView GetReserve(ReserveInputForm reserveInputForm, ModelAndView mv) {
		Object enameObject = session.getAttribute("ename");
		String enameString = null;
		if (enameObject != null) {
			enameString = enameObject.toString(); // toString()メソッドでStringに変換
		}
		List<Reserve> reserveList = reserveRepository.findAllByEname(enameString);
		int dateRange = 7;
		int timeRange = 10;
		LocalDate startDate = LocalDate.now();
		LocalDate endDate = startDate.plusDays(dateRange);
		LocalTime startTime = LocalTime.of(10, 0);
		LocalTime endTime = startTime.plusHours(timeRange);
		System.out.println(startDate);
		System.out.println(endDate);
		System.out.println(startTime);
		System.out.println(endTime);

		List<List<Integer>> matrix = new ArrayList<>();
		List<LocalDate> dateList = new ArrayList<>();
		List<LocalTime> timeList = new ArrayList<>();

		for (int i = 0; i < dateRange + 1; i++) {
			LocalDate currentDate = startDate.plusDays(i);
			dateList.add(currentDate);
		}

		for (int j = 0; j < timeRange + 1; j++) {
			LocalTime currentTime = startTime.plusHours(j);
			timeList.add(currentTime);
		}

		// 結果の出力（確認用）
		System.out.println("Dates:");
		for (LocalDate date : dateList) {
			System.out.println(date);
		}

		System.out.println("\nTimes:");
		for (LocalTime time : timeList) {
			System.out.println(time);
		}

		for (int i = 0; i < timeRange + 1; i++) {
			List<Integer> row = new ArrayList<>(dateRange + 1); // 列数を指定してArrayListを生成

			// 各列を0で初期化
			for (int j = 0; j < dateRange + 1; j++) {
				row.add(0);
			}

			// 行を二重リストに追加
			matrix.add(row);
		}
		for (Reserve i : reserveList) {

			LocalDate reserveDate = i.getReservedate();
			LocalTime reserveTime = i.getReservetime();

			System.out.println(reserveDate);
			System.out.println(reserveTime);
			int daysDifference = (int) ChronoUnit.DAYS.between(startDate, reserveDate);
			int timeDifference = (int) ChronoUnit.HOURS.between(startTime, reserveTime);

			System.out.println(startDate);
			System.out.println(reserveDate);
			System.out.println(daysDifference);

			System.out.println(timeDifference);

			if (reserveDate.isBefore(LocalDate.now()) || dateRange < ChronoUnit.DAYS.between(startDate, reserveDate)) {
				continue;
			}

			matrix.get(timeDifference).set(daysDifference, 1);

		}

		// 二重リストの内容を出力（確認用）
		for (List<Integer> row : matrix) {
			System.out.println(row);
		}
		
		mv.addObject("reserveInputForm", reserveInputForm);

		mv.addObject("matrix", matrix);
		mv.addObject("dateList", dateList);
		mv.addObject("timeList", timeList);
		mv.setViewName("reserveInput");

		return mv;
	}

	@GetMapping("/Register")
	public String showCustomerForm(CustomerInputForm customerInputForm) {
		return "customerInput";
	}

	@PostMapping("/Customer")
	public ModelAndView showCustomerList(@ModelAttribute @Validated LoginForm loginForm,
			BindingResult result, ModelAndView mv) {

		Customer customer = customerService.getByCid(loginForm, result);

		if (!result.hasErrors()) {
			session.setAttribute("cname", customer.getCname());
			List<ReserveCustomer> list = reserveCustomerRepository.findAIIBycustomerId(customer.getCid());
			mv.addObject("reserveList", list);
			mv.setViewName("customer");
			return mv;
		} else {
			mv.setViewName("login");
			return mv;
		}
	}

	@PostMapping("/Complete")
	public ModelAndView PostCustomer(@ModelAttribute @Validated ReserveInputForm reserveInputForm, BindingResult result,
			RedirectAttributes redirectAttributes,
			ModelAndView mv) {
		customerService.getByCid(reserveInputForm, result);

		if (!result.hasErrors()) {
			Reserve reserve = reserveInputForm.getEntity();
			reserveRepository.saveAndFlush(reserve);
			mv.setViewName("complete");
			return mv;
		} else {
			mv.setViewName("reserveInput");
			return mv;
		}

	}

	@PostMapping("/setCustomer")
	public String PostComplete(@ModelAttribute CustomerInputForm customerInputForm, BindingResult result) {
		if (!result.hasErrors()) {
			String cname = customerInputForm.getCname();
			session.setAttribute("cid", cname);
			Customer customer = customerInputForm.getEntity();
			customerRepository.saveAndFlush(customer);
			return "redirect:/Reserve";
		} else {
			return "customerInput";
		}
	}

	@PostMapping("/setEname")
	public String PostReserve(@RequestParam("ename") String ename) {
		session.setAttribute("ename", ename);

		return "redirect:/Reserve";

	}

}
