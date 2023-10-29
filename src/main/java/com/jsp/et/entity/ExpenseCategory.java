package com.jsp.et.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "category_table")
public class ExpenseCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int categoryId;

	@Column(unique = true)
	private String category;

	public ExpenseCategory(String category) {
		super();
		this.category = category;
	}

	@OneToMany(mappedBy = "expenseCategory")
	private List<Expenses> expenses;
}









