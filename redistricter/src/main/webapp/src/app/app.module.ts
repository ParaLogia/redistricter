import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { HttpModule } from '@angular/http';

import { LeafletModule } from '@asymmetrik/ngx-leaflet';

import { MatCardModule } from '@angular/material';
import { MatTabsModule } from '@angular/material';
import { MatSliderModule } from '@angular/material/slider';
import { MatToolbarModule } from '@angular/material';
import { MatButtonModule } from '@angular/material';
import { MatSelectModule } from '@angular/material';
import { MatInputModule } from '@angular/material';
import { MatSlideToggleModule } from '@angular/material';
import { MatTableModule } from '@angular/material/table';

import { AppComponent } from './app.component';
import { MapComponentComponent } from './map-component/map-component.component';
import { LoginComponentComponent } from './login-component/login-component.component';
import { AdminHomeComponent } from './admin-home/admin-home.component';
import { HttpClientModule } from '@angular/common/http';


const appRoutes: Routes = [
  { path: '', component: MapComponentComponent },
  { path: 'login', component: LoginComponentComponent },
  { path: 'admin/home', component: AdminHomeComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    MapComponentComponent,
    LoginComponentComponent,
    AdminHomeComponent
  ],
  imports: [
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forRoot(appRoutes),
    BrowserModule,
    LeafletModule.forRoot(),
    MatCardModule,
    MatTabsModule,
    MatToolbarModule,
    MatSliderModule,
    MatButtonModule,
    MatSelectModule,
    MatInputModule,
    MatSlideToggleModule,
    MatTableModule,
    HttpModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
