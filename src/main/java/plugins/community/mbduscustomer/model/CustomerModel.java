/*******************************************************************************
 * MIT License
 *
 * Companyware - a java desktop framework for plugins
 *
 * Copyright (c) 2023 mbdus-Softwareentwicklung - Mathias Bauer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package plugins.community.mbduscustomer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import plugins.core.plugins.model.DisplayAs;

@Entity
@Table(name = "Customer")
public class CustomerModel {

	public CustomerModel() {
		// TODO Auto-generated constructor stub
	}
	
    private Long id;
    private String salutation;
    private String name;
    private String firstname;
    private String email;
    private String street;
    private String streetnumber;
    private String zipcode;
    private String city;
    private String country;
    private JCheckBox action;
    private JButton button;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @DisplayAs(value = "customer.salutation", index = 0, editable = false)
    public String getSalutation() {
        return salutation;
    }
    
    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    @DisplayAs(value = "customer.name", index = 1, editable = false)
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @DisplayAs(value = "customer.firstname", index = 2, editable = false)
    public String getFirstname() {
        return firstname;
    }
    
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    
    @DisplayAs(value = "customer.email", index = 3, editable = false)
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    @DisplayAs(value = "customer.street", index = 4, editable = false)
    public String getStreet() {
        return street;
    }
    
    public void setStreet(String street) {
        this.street = street;
    }
  
    @DisplayAs(value = "customer.streetnumber", index = 5, editable = false)
    public String getStreetnumber() {
        return streetnumber;
    }
    
    public void setStreetnumber(String streetnumber) {
        this.streetnumber = streetnumber;
    }
    
    @DisplayAs(value = "customer.zipcode", index = 6, editable = false)
    public String getZipcode() {
        return zipcode;
    }
    
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
    
    @DisplayAs(value = "customer.city", index = 7, editable = false)
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    @DisplayAs(value = "customer.country", index = 8, editable = false)
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    @DisplayAs(value = "", index = 9, editable = true)
    public JCheckBox getAction() {
        return action;
    }

    public void setAction(JCheckBox action) {
        this.action = action;
    }
    
    @DisplayAs(value = "", index = 10, editable = true)
    public JButton getButton() {
		return button;
	}

	public void setButton(JButton button) {
		this.button = button;
	}
    
    @Override
    public String toString() {
        return String.format("CustomerModel Id: %d Name: %s Firstname: %d City: %s", 
                id, name, firstname, city);
    }
}