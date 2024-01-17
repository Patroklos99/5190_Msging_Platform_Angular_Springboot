import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewMsgFormComponent } from './new-msg-form.component';

describe('NewMsgFormComponent', () => {
  let component: NewMsgFormComponent;
  let fixture: ComponentFixture<NewMsgFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NewMsgFormComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NewMsgFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
