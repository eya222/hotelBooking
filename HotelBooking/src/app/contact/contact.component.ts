import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.scss']
})
export class ContactComponent implements OnInit {
  contactForm: FormGroup;
  submitted = false;
  showSuccessMessage = false;

  constructor(private formBuilder: FormBuilder) {
    this.contactForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      subject: ['', [Validators.required, Validators.minLength(5)]],
      message: ['', [Validators.required, Validators.minLength(20)]]
    });
  }

  ngOnInit(): void {}

  // Getter for easy access to form fields
  get f() {
    return this.contactForm.controls;
  }

  onSubmit() {
    this.submitted = true;

    // stop here if form is invalid
    if (this.contactForm.invalid) {
      return;
    }

    // TODO: Implement actual form submission logic here
    console.log('Form Data:', this.contactForm.value);

    // Show success message
    this.showSuccessMessage = true;

    // Reset form after 3 seconds
    setTimeout(() => {
      this.submitted = false;
      this.showSuccessMessage = false;
      this.contactForm.reset();
    }, 3000);
  }

  onReset() {
    this.submitted = false;
    this.showSuccessMessage = false;
    this.contactForm.reset();
  }

  // Helper method to check if a field has errors
  hasError(field: string, error: string): boolean {
    return this.submitted && this.contactForm.get(field)?.errors?.[error];
  }

  // Helper method to get field value
  getFieldValue(field: string): any {
    return this.contactForm.get(field)?.value;
  }
}
